package com.invoice.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.invoice.api.dto.DtoCartItem;
import com.invoice.api.entity.Cart;
import com.invoice.api.service.SvcCart;
import com.invoice.commons.dto.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Controlador REST para la gestión del Carrito de Compras.
 * Permite manipular los ítems temporales antes de la compra.
 */
@RestController
@RequestMapping("/cart-item")
public class CtrlCart {

    @Autowired
    SvcCart svc;

    /**
     * Método auxiliar para extraer el token JWT del encabezado de la petición HTTP.
     * Elimina el prefijo "Bearer " para obtener solo la cadena del token.
     * 
     * @param request La petición HTTP entrante (HttpServletRequest).
     * @return La cadena del token limpia, o null si no existe el encabezado.
     */
    private String getToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    /**
     * Recupera el carrito de compras del usuario actual.
     * Enriquece la información con datos del Product-Service (Nombre, Precio,
     * Descripción).
     * 
     * @param request Petición HTTP para extraer el token.
     * @return Lista de DtoCartItem con detalles extendidos.
     */
    @GetMapping
    public ResponseEntity<List<DtoCartItem>> getCart(HttpServletRequest request) {
        return ResponseEntity.ok(svc.getCart(getToken(request)));
    }

    /**
     * Agrega un producto al carrito o actualiza su cantidad si ya existe.
     * Valida stock en tiempo real contra Product-Service.
     * 
     * @param cart    Objeto con GTIN y cantidad.
     * @param request Petición HTTP para extraer el token.
     * @return Mensaje de éxito.
     */
    @PostMapping
    public ResponseEntity<ApiResponse> addToCart(@RequestBody Cart cart, HttpServletRequest request) {
        return ResponseEntity.ok(svc.addToCart(getToken(request), cart));
    }

    /**
     * Elimina un ítem específico del carrito.
     * 
     * @param id      ID del registro en cart_item.
     * @param request Petición HTTP para extraer el token.
     * @return Mensaje de éxito.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> removeFromCart(@PathVariable("id") Integer id, HttpServletRequest request) {
        return ResponseEntity.ok(svc.removeFromCart(getToken(request), id));
    }

    /**
     * Vacía completamente el carrito del usuario.
     * 
     * @param request Petición HTTP para extraer el token.
     * @return Mensaje de éxito.
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse> clearCart(HttpServletRequest request) {
        return ResponseEntity.ok(svc.clearCart(getToken(request)));
    }
}
