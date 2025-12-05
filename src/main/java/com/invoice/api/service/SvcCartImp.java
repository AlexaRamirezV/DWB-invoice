package com.invoice.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.invoice.api.dto.DtoCartItem;
import com.invoice.api.dto.DtoProduct;
import com.invoice.api.entity.Cart;
import com.invoice.api.repository.RepoCart;
import com.invoice.api.service.product.ProductClient;
import com.invoice.commons.dto.ApiResponse;
import com.invoice.commons.util.JwtDecoder;
import com.invoice.exception.ApiException;

/**
 * Implementación de la lógica de negocio del Carrito.
 * 
 * Esta clase interactúa con el repositorio local (RepoCart) y se comunica
 * con el microservicio de Productos mediante (ProductClient) para obtener
 * información actualizada y validar inventarios en tiempo real.
 */
@Service
public class SvcCartImp implements SvcCart {

    @Autowired
    RepoCart repo;

    @Autowired
    ProductClient productClient;

    @Autowired
    JwtDecoder jwtDecoder;

    /**
     * Recupera los items del carrito y consulta product-service para llenar los
     * detalles (Nombre, Descripción, Precio actual) que no se guardan en la tabla
     * local.
     */
    @Override
    public List<DtoCartItem> getCart(String token) {
        Integer userId = jwtDecoder.extractUserId(token); // Asumiendo que adaptaste JwtDecoder para recibir el token
                                                          // crudo o lo sacas del contexto
        // 1. Obtener items crudos de la BD (solo tienen GTIN y cantidad)
        List<Cart> cartList = repo.findByUserId(userId);
        List<DtoCartItem> dtoCartList = new ArrayList<>();

        // 2. Iterar cada item para buscar sus detalles en la API de Productos
        for (Cart cart : cartList) {
            DtoCartItem dto = new DtoCartItem();
            dto.setGtin(cart.getGtin());
            dto.setQuantity(cart.getQuantity());

            // Llamada a product-service para obtener nombre y precio
            try {
                ResponseEntity<DtoProduct> response = productClient.getProduct(cart.getGtin());
                DtoProduct productData = response.getBody();
                
                dto.setProduct(productData.getProduct()); // Nombre
                dto.setDescription(productData.getDescription());
                dto.setUnit_price(productData.getPrice()); // Precio unitario
                dto.setTotal(productData.getPrice() * cart.getQuantity()); // Total
                
            } catch (Exception e) {
                // Si falla la conexión con productos, ponemos valores por defecto para no romper el carrito
                dto.setProduct("Producto no disponible");
                dto.setUnit_price(0.0);
                dto.setTotal(0.0);
            }

            dtoCartList.add(dto);
        }

        return dtoCartList;
    }

    /**
     * Agrega un ítem al carrito.
     * Valida existencia y stock disponible en product-service antes de guardar.
     * Si el producto ya está en el carrito, suma la cantidad y re-valida el stock
     * total.
     */
    @Override
    public ApiResponse addToCart(String token, Cart cartItem) {
        // 1. Obtener usuario del token
        Integer userId = jwtDecoder.extractUserId(token);

        // 2. Validar cantidad válida
        if (cartItem.getQuantity() <= 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor a 0");
        }

        // 3. Validar producto con la API PRODUCT
        DtoProduct product;
        try {
            ResponseEntity<DtoProduct> response = productClient.getProduct(cartItem.getGtin());
            product = response.getBody();
        } catch (Exception e) {
            System.err.println("ESTE ES EL ERROR REAL: " + e.getMessage());
            e.printStackTrace();
            throw new ApiException(HttpStatus.NOT_FOUND, "El producto no existe en el catálogo");
        }

        // 4. Validar Stock Inicial
        if (product.getStock() < cartItem.getQuantity()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Stock insuficiente. Disponible: " + product.getStock());
        }

        // 5. Verificar si ya existe en el carrito para sumar cantidad o crear nuevo
        Optional<Cart> existing = repo.findByUserIdAndGtin(userId, cartItem.getGtin());

        if (existing.isPresent()) {
            Cart currentItem = existing.get();
            Integer newQuantity = currentItem.getQuantity() + cartItem.getQuantity();

            // Validar stock con la nueva cantidad total
            if (product.getStock() < newQuantity) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Stock insuficiente para la cantidad total deseada");
            }

            currentItem.setQuantity(newQuantity);
            repo.save(currentItem);
        } else {
            cartItem.setUser_id(userId);
            cartItem.setStatus(1); // Activo
            repo.save(cartItem);
        }

        return new ApiResponse("Producto agregado al carrito");
    }

    /**
     * Elimina un artículo específico del carrito de compras del usuario.
     * 
     * @param token  Token JWT para identificar al usuario dueño del carrito.
     * @param cartId ID único del registro del ítem en el carrito (cart_item_id).
     * @return ApiResponse con el mensaje de éxito o error si no se encuentra el
     *         ítem.
     */
    @Override
    public ApiResponse removeFromCart(String token, Integer cartId) {
        Integer userId = jwtDecoder.extractUserId(token);
        if (repo.removeFromCart(cartId, userId) > 0)
            return new ApiResponse("Item eliminado");
        else
            throw new ApiException(HttpStatus.NOT_FOUND, "Item no encontrado en tu carrito");
    }

    /**
     * Vacía completamente el carrito de compras del usuario.
     * Elimina todos los registros asociados al ID del usuario extraído del token.
     * Se utiliza típicamente al finalizar una compra exitosa o cuando el usuario
     * decide limpiar su carrito.
     * 
     * @param token Token JWT para identificar al usuario.
     * @return ApiResponse confirmando la limpieza del carrito.
     */
    @Override
    public ApiResponse clearCart(String token) {
        Integer userId = jwtDecoder.extractUserId(token);
        repo.clearCart(userId);
        return new ApiResponse("Carrito vaciado");
    }


}
