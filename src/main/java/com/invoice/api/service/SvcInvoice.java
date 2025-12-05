package com.invoice.api.service;

import java.util.List;

import com.invoice.commons.dto.ApiResponse;
import com.invoice.api.dto.DtoInvoiceIn;
import com.invoice.api.dto.DtoInvoiceList;
import com.invoice.api.entity.Invoice;

/**
 * Interfaz que define la lógica de negocio para la generación y consulta de
 * facturas.
 * 
 * Encargada de orquestar el proceso de finalización de compra y el historial de
 * transacciones.
 * 
 */
public interface SvcInvoice {

	/**
	 * Obtiene el listado de facturas.
	 * Implementa lógica para diferenciar entre administradores (ven todas) y
	 * clientes (ven las propias).
	 * 
	 * @return Lista de DtoInvoiceList con resúmenes de facturas.
	 */
	public List<DtoInvoiceList> findAll();

	/**
	 * Busca una factura específica por su ID.
	 * 
	 * @param id ID de la factura.
	 * @return La entidad Invoice completa con sus detalles (items).
	 */
	public Invoice findById(Integer id);

	/**
	 * Ejecuta el proceso transaccional de compra (Checkout).
	 * Convierte los ítems del carrito en una factura formal.
	 * 
	 * @param token Token JWT del usuario.
	 * @param dto   Datos opcionales de pago y descuentos.
	 * @return ApiResponse con el resultado de la transacción.
	 */
	public ApiResponse create(String token, DtoInvoiceIn dto);
}
