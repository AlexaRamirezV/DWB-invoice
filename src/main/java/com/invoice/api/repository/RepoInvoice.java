package com.invoice.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.invoice.api.entity.Invoice;

/**
 * Repositorio JPA para la gestión de la persistencia de las Facturas.
 * 
 * Permite guardar nuevas facturas (con sus ítems en cascada) y consultar
 * el historial de compras filtrado por el ID del usuario.
 */
@Repository
public interface RepoInvoice extends JpaRepository<Invoice, Integer> {

	@Query("SELECT i FROM Invoice i WHERE i.user_id = :user_id")
    List<Invoice> findAllByUserId(@Param("user_id") Integer user_id);	
}
