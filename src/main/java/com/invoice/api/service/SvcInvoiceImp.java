package com.invoice.api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.DtoCustomer;
import com.invoice.api.dto.DtoInvoiceIn;
import com.invoice.api.dto.DtoInvoiceList;
import com.invoice.api.dto.DtoProduct;
import com.invoice.api.entity.Cart;
import com.invoice.api.entity.Invoice;
import com.invoice.api.entity.InvoiceItem;
import com.invoice.api.repository.RepoCart;
import com.invoice.api.repository.RepoInvoice;
import com.invoice.api.service.customer.CustomerClient;
import com.invoice.api.service.product.ProductClient;
import com.invoice.commons.mapper.MapperInvoice;
import com.invoice.commons.util.JwtDecoder;
import com.invoice.exception.ApiException;
import com.invoice.exception.DBAccessException;

import jakarta.transaction.Transactional;

@Service
public class SvcInvoiceImp implements SvcInvoice {

	@Autowired
	private RepoInvoice repoInvoice;

	@Autowired
    private RepoCart repoCart;

	@Autowired
    private ProductClient productClient;

	@Autowired
	private JwtDecoder jwtDecoder;

	@Autowired
	MapperInvoice mapper;

	@Autowired
    private CustomerClient customerClient;


	@Override
	public List<DtoInvoiceList> findAll() {
		try {
			if (jwtDecoder.isAdmin()) {
				return mapper.toDtoList(repoInvoice.findAll());
			} else {
				Integer user_id = jwtDecoder.getUserId();
				return mapper.toDtoList(repoInvoice.findAllByUserId(user_id));
			}
		} catch (DataAccessException e) {
			throw new DBAccessException();
		}
	}

	@Override
	public Invoice findById(Integer id) {
		try {
			Invoice invoice = repoInvoice.findById(id).get();
			if (!jwtDecoder.isAdmin()) {
				Integer user_id = jwtDecoder.getUserId();
				if (invoice.getUser_id() != user_id) {
					throw new ApiException(HttpStatus.FORBIDDEN, "El token no es válido para consultar esta factura");
				}
			}
			return invoice;
		} catch (DataAccessException e) {
			throw new DBAccessException();
		} catch (NoSuchElementException e) {
			throw new ApiException(HttpStatus.NOT_FOUND, "El id de la factura no existe");
		}
	}

	@Override
	@Transactional
	public ApiResponse create(String token, DtoInvoiceIn dto) { // Pasamos el token desde el controlador
		
		// Obtener el ID del usuario desde el token
		Integer userId = jwtDecoder.extractUserId(token);

		// Punto extra
		//  Ir a Customer Service por la dirección
		String shippingAddress = "";
		try {
            // Buscamos al cliente en la otra API con el mismo ID del usuario
            ResponseEntity<DtoCustomer> response = customerClient.getCustomer(userId);
            shippingAddress = response.getBody().getAddress();
        } catch (Exception e) {
			System.err.println("ERROR AL LLAMAR A CUSTOMER: " + e.getMessage());
			e.printStackTrace();
            throw new ApiException(HttpStatus.BAD_REQUEST, "No se encontró información del cliente para este usuario. Asegúrese de tener un perfil creado en el módulo de Clientes");
        }

		// Obtener items del carrito
		List<Cart> cartItems = repoCart.findByUserId(userId);
		if (cartItems.isEmpty()) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "El carrito está vacío");
		}

		// Preparar la factura
		Invoice invoice = new Invoice();

		invoice.setUser_id(userId);
		invoice.setCreated_at(LocalDateTime.now().toString());
		invoice.setStatus(1); // Activa

		// Puntos extra

		// Dirección de envío
		invoice.setShipping_address(shippingAddress);

		// Método de Pago
		List<String> validMethods = Arrays.asList("EFECTIVO", "TARJETA", "PAYPAL", "TRANSFERENCIA");
		String method = (dto != null && dto.getPayment_method() != null) ? dto.getPayment_method().toUpperCase()
				: "EFECTIVO";

		if (!validMethods.contains(method)) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "Método de pago inválido. Disponibles: " + validMethods);
		}

		invoice.setPayment_method(method);

		// Lógica específica para TARJETA
		if (method.equals("TARJETA")) {
			if (dto.getCard_number() == null || dto.getCard_number().length() < 16) {
				throw new ApiException(HttpStatus.BAD_REQUEST, "Para pago con tarjeta se requieren los 16 dígitos.");
			}

			// SIMULACIÓN DE SEGURIDAD: Solo guardamos los últimos 4 dígitos
			String rawCard = dto.getCard_number();
			String maskedCard = "************" + rawCard.substring(rawCard.length() - 4);

			invoice.setCard_number(maskedCard);
		} else {
			invoice.setCard_number(null); // No aplica para efectivo/paypal
		}

		// Cupones
		// Definimos un descuento inicial de 0
		Double discountPercentage = 0.0;
		String coupon = (dto != null && dto.getCoupon_code() != null) ? dto.getCoupon_code().toUpperCase() : null;

		// Definimos nuestros  cupones válidos en memoria
        Map<String, Double> validCoupons = new HashMap<>();
        validCoupons.put("DESCUENTO10", 0.10); // 10%
        validCoupons.put("DESCUENTO20", 0.20); // 20% <--- AHORA SÍ VA A FUNCIONAR ESTE
        validCoupons.put("DESCUENTO50", 0.50); // 50%
        validCoupons.put("BUENFIN20",   0.20); // 20%

        // Verificamos si el cupón que mandó el usuario existe en nuestro mapa
        if (coupon != null && validCoupons.containsKey(coupon)) {
            discountPercentage = validCoupons.get(coupon);
            invoice.setCoupon_code(coupon);
        } else if (coupon != null) {
            // Opcional: Si mandó un cupón pero no es válido, podemos dejarlo en null 
           
        }



		List<InvoiceItem> items = new ArrayList<>();
		Double subtotal = 0.0;
		Double totalTaxes = 0.0;

		// Procesar cada item
		for (Cart c : cartItems) {

			// Consultar producto en la API product
			DtoProduct product;
			try {
				product = productClient.getProduct(c.getGtin()).getBody();
			} catch (Exception e) {
				throw new ApiException(HttpStatus.BAD_REQUEST, "Error al obtener producto: " + c.getGtin());
			}

			// Validar Stock actual (pudo haber cambiado desde que se agrego al carrito)
			if (product.getStock() < c.getQuantity()) {
				throw new ApiException(HttpStatus.BAD_REQUEST, "Stock insuficiente para el producto: " + c.getGtin());
			}

			// Cálculos
			Double unitPrice = product.getPrice();
			Double itemTotal = unitPrice * c.getQuantity();
			Double itemTax = itemTotal * 0.16; // 16% IVA
			Double itemSubtotal = itemTotal - itemTax;

			subtotal += itemSubtotal;
			totalTaxes += itemTax;

			// Crear InvoiceItem
			InvoiceItem item = new InvoiceItem();
			item.setGtin(c.getGtin());
			item.setQuantity(c.getQuantity());
			item.setUnit_price(unitPrice);
			item.setSubtotal(itemSubtotal);
			item.setTaxes(itemTax);
			item.setTotal(itemTotal);
			item.setStatus(1);

			items.add(item);

			// RESTAR STOCK EN API PRODUCTOS
			productClient.updateProductStock(c.getGtin(), c.getQuantity());
		}

		// --- APLICAR DESCUENTO AL FINAL ---
		Double discountAmount = subtotal * discountPercentage; // Descuento sobre subtotal
		Double subtotalWithDiscount = subtotal - discountAmount;

		// Totales finales de la factura
		invoice.setSubtotal(subtotal);
		invoice.setDiscount(discountAmount); // Guardamos cuánto se descontó
		invoice.setTaxes(totalTaxes);

		// Total = (Subtotal - Descuento) + Impuestos
		invoice.setTotal(subtotalWithDiscount + totalTaxes);

		invoice.setItems(items);

		// Guardar Factura
		repoInvoice.save(invoice);

		// Vaciar Carrito
		repoCart.clearCart(userId);

		return new ApiResponse("Compra finalizada exitosamente");
	}

}
