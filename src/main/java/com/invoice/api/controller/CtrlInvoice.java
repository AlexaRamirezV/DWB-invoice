package com.invoice.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.DtoInvoiceIn;
import com.invoice.api.dto.DtoInvoiceList;
import com.invoice.api.entity.Invoice;
import com.invoice.api.service.SvcInvoice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/invoice")
@Tag(name = "Invoice", description = "Administración de facturas")
public class CtrlInvoice {

	@Autowired
	SvcInvoice svc;

	@GetMapping
	@Operation(summary = "Consulta de facturas", description = "Administrador consulta todas las facturas. Cliente consulta sus facturas.")
	public ResponseEntity<List<DtoInvoiceList>> findAll() {	
		return ResponseEntity.ok(svc.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Consulta de factura", description = "Consulta el detalle de una factura")
	public ResponseEntity<Invoice> findById(@PathVariable("id") Integer id) {		
		return ResponseEntity.ok(svc.findById(id));
	}
	
	@PostMapping
	@Operation(summary = "Creación de factura", description = "Cliente crea una factura. Puede incluir método de pago y cupón (la dirección de envío se rescata de la API customer)")
	// 'required = false' para que no falle si no mandan nada
	public ResponseEntity<ApiResponse> create(HttpServletRequest request, @RequestBody(required = false) DtoInvoiceIn dto){ // Recibimos el request
        // Extraemos el token quitando la palabra "Bearer "
        String token = request.getHeader("Authorization").substring(7);
        
		return ResponseEntity.ok(svc.create(token, dto)); // Pasamos el token al servicio
	}
	
}
