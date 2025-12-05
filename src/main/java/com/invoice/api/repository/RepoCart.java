package com.invoice.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.invoice.api.entity.Cart;
import jakarta.transaction.Transactional;

/**
 * Repositorio JPA para la gestión de la persistencia del Carrito de Compras.
 * Provee métodos para buscar ítems activos por usuario, verificar si un producto
 * ya existe en el carrito y limpiar el carrito tras una compra.
 */
@Repository
public interface RepoCart extends JpaRepository<Cart, Integer> {

    // Buscar todos los items activos del usuario
    @Query(value = "SELECT * FROM cart_item WHERE user_id = :user_id AND status = 1", nativeQuery = true)
    List<Cart> findByUserId(@Param("user_id") Integer user_id);

    // Buscar si un producto ya existe en el carrito del usuario
    @Query(value = "SELECT * FROM cart_item WHERE user_id = :user_id AND gtin = :gtin AND status = 1", nativeQuery = true)
    Optional<Cart> findByUserIdAndGtin(@Param("user_id") Integer user_id, @Param("gtin") String gtin);

    // Borrar lógicamente un item (status = 0)
    @Modifying
    @Transactional
    @Query(value = "UPDATE cart_item SET status = 0 WHERE cart_item_id = :cart_id AND user_id = :user_id", nativeQuery = true)
    Integer removeFromCart(@Param("cart_id") Integer cart_id, @Param("user_id") Integer user_id);

    // Vaciar carrito (borrado lógico o físico, aquí uso físico para limpiar la BD
    // tras la compra)
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cart_item WHERE user_id = :user_id", nativeQuery = true)
    void clearCart(@Param("user_id") Integer user_id);
}