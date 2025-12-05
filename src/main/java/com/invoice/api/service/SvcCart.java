package com.invoice.api.service;

import java.util.List;

import com.invoice.api.dto.DtoCartItem;
import com.invoice.api.entity.Cart;
import com.invoice.commons.dto.ApiResponse;

/**
 * Interfaz que define la lógica de negocio para la gestión del Carrito de
 * Compras.
 * 
 * Provee los métodos necesarios para manipular los artículos temporales que el
 * usuario
 * desea adquirir antes de proceder a la facturación.
 * 
 */
public interface SvcCart {

    /**
     * Recupera el contenido del carrito de compras del usuario.
     * 
     * @param token Token JWT para extraer el ID del usuario.
     * @return Lista de DtoCartItem enriquecida con datos del catálogo (nombre,
     *         precio, descripción).
     */
    public List<DtoCartItem> getCart(String token);

    /**
     * Agrega un artículo al carrito o incrementa su cantidad si ya existe.
     * Debe validar la existencia del producto y el stock suficiente.
     * 
     * @param token    Token JWT del usuario.
     * @param cartItem Entidad con el GTIN y la cantidad deseada.
     * @return ApiResponse indicando el éxito de la operación.
     */
    public ApiResponse addToCart(String token, Cart cartItem);

    /**
     * Elimina un artículo específico del carrito.
     * 
     * @param token  Token JWT del usuario.
     * @param cartId ID único del registro en el carrito.
     * @return ApiResponse confirmando la eliminación.
     */
    public ApiResponse removeFromCart(String token, Integer cartId);

    /**
     * Elimina todos los artículos del carrito del usuario.
     * 
     * @param token Token JWT del usuario.
     * @return ApiResponse confirmando la limpieza total.
     */
    public ApiResponse clearCart(String token);
}