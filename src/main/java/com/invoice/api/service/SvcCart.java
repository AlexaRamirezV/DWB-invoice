package com.invoice.api.service;

import java.util.List;

import com.invoice.api.dto.DtoCartItem;
import com.invoice.api.entity.Cart;
import com.invoice.commons.dto.ApiResponse;

public interface SvcCart {

    // Devuelve el carrito de un usuario específico
    public List<DtoCartItem> getCart(String token);

    // Agrega item al carrito
    public ApiResponse addToCart(String token, Cart cartItem);

    // Quita item del carrito
    public ApiResponse removeFromCart(String token, Integer cartId);

    // Vacía el carrito
    public ApiResponse clearCart(String token);
}