package com.invoice.api.service.customer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.invoice.api.dto.DtoCustomer;

/**
 * Cliente Feign para la comunicación con el microservicio de Clientes
 * (customer-service).
 * Se utiliza para obtener datos de perfil del usuario.
 */
@FeignClient(name = "customer") // Nombre en Eureka
public interface CustomerClient {
    /**
     * Obtiene la información de un cliente por su ID.
     * Se utiliza en el proceso de facturación para obtener la dirección de envío.
     * 
     * @param id ID del cliente.
     * @return DTO con dirección y RFC.
     */
    @GetMapping("/customer/{id}") // El endpoint que ya está en Customer
    ResponseEntity<DtoCustomer> getCustomer(@PathVariable("id") Integer id);
    
}
