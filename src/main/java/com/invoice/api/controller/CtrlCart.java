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

@RestController
@RequestMapping("/cart-item")
public class CtrlCart {

    @Autowired
    SvcCart svc;

    // Helper para obtener el token puro (sin Bearer)
    private String getToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    @GetMapping
    public ResponseEntity<List<DtoCartItem>> getCart(HttpServletRequest request) {
        return ResponseEntity.ok(svc.getCart(getToken(request)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addToCart(@RequestBody Cart cart, HttpServletRequest request) {
        return ResponseEntity.ok(svc.addToCart(getToken(request), cart));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> removeFromCart(@PathVariable("id") Integer id, HttpServletRequest request) {
        return ResponseEntity.ok(svc.removeFromCart(getToken(request), id));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> clearCart(HttpServletRequest request) {
        return ResponseEntity.ok(svc.clearCart(getToken(request)));
    }
}
