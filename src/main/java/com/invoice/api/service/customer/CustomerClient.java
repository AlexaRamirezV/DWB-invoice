package com.invoice.api.service.customer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.invoice.api.dto.DtoCustomer;

@FeignClient(name = "customer") // Nombre en Eureka
public interface CustomerClient {
    @GetMapping("/customer/{id}") // El endpoint que ya está en Customer
    ResponseEntity<DtoCustomer> getCustomer(@PathVariable("id") Integer id);
}
