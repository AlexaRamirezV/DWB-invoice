package com.invoice.api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.DtoInvoiceIn;
import com.invoice.api.dto.DtoInvoiceList;
import com.invoice.api.dto.DtoProduct;
import com.invoice.api.entity.Cart;
import com.invoice.api.entity.Invoice;
import com.invoice.api.entity.InvoiceItem;
import com.invoice.api.repository.RepoCart;
import com.invoice.api.repository.RepoInvoice;
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
	public Invoice findById(String id) {
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
	public ApiResponse create(String token, DtoInvoiceIn dto) { // Asegúrate de pasar el token desde el controlador
		Integer userId = jwtDecoder.extractUserId(token);

		// --- VALIDACIÓN DE DIRECCIÓN (Punto Extra) ---
		if (dto.getShipping_address() == null || dto.getShipping_address().isEmpty()) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "La dirección de envío es obligatoria");
		}

		// 1. Obtener items del carrito
		List<Cart> cartItems = repoCart.findByUserId(userId);
		if (cartItems.isEmpty()) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "El carrito está vacío");
		}

		// 2. Preparar la factura
		Invoice invoice = new Invoice();

		// Generamos un ID único manual porque es VARCHAR en la BD
		String uniqueId = java.util.UUID.randomUUID().toString();
		invoice.setInvoice_id(uniqueId);

		invoice.setUser_id(userId);
		invoice.setCreated_at(LocalDateTime.now().toString());
		invoice.setStatus(1); // Activa

		// PUNTO EXTRA
		invoice.setShipping_address(dto.getShipping_address());

		List<InvoiceItem> items = new ArrayList<>();
		Double subtotal = 0.0;
		Double totalTaxes = 0.0;

		// 3. Procesar cada item
		for (Cart c : cartItems) {
			// Validar Stock actual (pudo haber cambiado desde que lo agregó al carrito)
			DtoProduct product = productClient.getProduct(c.getGtin()).getBody();
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
			item.setInvoice_id(uniqueId); // Asignar el ID de la factura al item
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

		// 4. Totales finales de la factura
		invoice.setSubtotal(subtotal);
		invoice.setTaxes(totalTaxes);
		invoice.setTotal(subtotal + totalTaxes);
		invoice.setItems(items); // JPA Cascade guardará los items y asignará el ID de factura automáticamente

		// 5. Guardar Factura
		repoInvoice.save(invoice);

		// 6. Vaciar Carrito
		repoCart.clearCart(userId);

		return new ApiResponse("Compra finalizada exitosamente");
	}
}
