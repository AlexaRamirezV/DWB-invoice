package com.invoice.api.service.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.invoice.api.dto.DtoProduct;
import com.invoice.commons.dto.ApiResponse;

/**
 * Cliente Feign para la comunicación con el microservicio de Productos
 * (product-service).
 * Se utiliza para consultar catálogo y gestionar inventario.
 */
// 'product' es el nombre en application.properties de Backend-proyecto
@FeignClient(name = "product", url = "http://localhost:8080")
public interface ProductClient {

    /**
     * Consulta un producto por su código GTIN.
     * 
     * @param gtin Código del producto.
     * @return DTO con información del producto (stock, precio, etc).
     */
    @GetMapping("/product/gtin/{gtin}")
    ResponseEntity<DtoProduct> getProduct(@PathVariable("gtin") String gtin);

    /**
     * Actualiza (resta) el stock de un producto tras una venta.
     * 
     * @param gtin     Código del producto.
     * @param quantity Cantidad a restar.
     * @return Respuesta de confirmación.
     */
    @PutMapping("/product/{gtin}/stock/{quantity}")
    ResponseEntity<ApiResponse> updateProductStock(@PathVariable("gtin") String gtin,
            @PathVariable("quantity") Integer quantity);
}
