package com.invoice.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Punto extra, recuperamos la dirección de API customer

@JsonIgnoreProperties(ignoreUnknown = true)
public class DtoCustomer {
    private String address;

    // Getters y Setters
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
