package com.invoice.api.service.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.invoice.api.dto.DtoProduct;
import com.invoice.commons.dto.ApiResponse;

// 'product' es el nombre en application.properties de Backend-proyecto
@FeignClient(name = "product", url = "http://localhost:8080") // url es opcional si usas eureka, pero útil para
                                                                      // pruebas locales
public interface ProductClient {

    @GetMapping("/product/gtin/{gtin}")
    ResponseEntity<DtoProduct> getProduct(@PathVariable("gtin") String gtin);

    @PutMapping("/product/{gtin}/stock/{quantity}")
    ResponseEntity<ApiResponse> updateProductStock(@PathVariable("gtin") String gtin,
            @PathVariable("quantity") Integer quantity);
}
