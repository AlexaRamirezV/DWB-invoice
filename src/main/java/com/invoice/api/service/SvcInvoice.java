package com.invoice.api.service;

import java.util.List;

import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.DtoInvoiceIn;
import com.invoice.api.dto.DtoInvoiceList;
import com.invoice.api.entity.Invoice;

public interface SvcInvoice {

	public List<DtoInvoiceList> findAll();
	public Invoice findById(String id);

	// CAMBIO AQUI: Ahora recibe el token para saber quién compra y punto extra DtoInvoiceIn
	public ApiResponse create(String token, DtoInvoiceIn dto);
}
