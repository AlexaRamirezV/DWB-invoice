package com.invoice.commons.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.invoice.api.dto.DtoInvoiceList;
import com.invoice.api.entity.Invoice;

/**
 * Componente encargado de la transformación de datos (Mapping) entre Entidades
 * y DTOs.
 * 
 * Se utiliza para convertir la información cruda de la base de datos (Entidad Invoice)
 * en objetos optimizados para la respuesta de la API (DtoInvoiceList).
 * Esto permite desacoplar la estructura interna de la base de datos de la respuesta pública
 * de la API.
 * 
 */
@Service
public class MapperInvoice {
	
	/**
	 * Convierte una lista de entidades de Factura en una lista de DTOs de
	 * respuesta.
	 * 
	 * Recorre cada factura recuperada de la base de datos y transfiere sus datos al
	 * objeto de transferencia, incluyendo los campos extendidos implementados como puntos extra (dirección,
	 * método de pago, descuentos).
	 * 
	 * @param invoices Lista de entidades Invoice recuperadas de la base de
	 *                 datos.
	 * @return Lista de objetos DtoInvoiceList listos para ser serializados
	 *         a JSON y enviados al cliente.
	 */
	public List<DtoInvoiceList> toDtoList(List<Invoice> invoices) {
		List<DtoInvoiceList> dtoInvoices = new ArrayList<>();
		
		for (Invoice invoice : invoices) {
			
			DtoInvoiceList dtoInvoice = new DtoInvoiceList(
		            invoice.getInvoice_id(),
		            invoice.getUser_id(),
		            invoice.getCreated_at(),
		            invoice.getSubtotal(),
		            invoice.getTaxes(),
		            invoice.getTotal(),
					invoice.getShipping_address(),
					invoice.getPayment_method(),
					invoice.getCard_number(),
					invoice.getCoupon_code(),
					invoice.getDiscount()
		        );
			dtoInvoices.add(dtoInvoice);
		}
         
         return dtoInvoices;
    }
}
