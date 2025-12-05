package com.invoice.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * Clase utilizada para el punto extra de dirección de envío:
 * Recuperamos la dirección de API customer
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DtoCustomer {
    private String address;

    /**
     * Método para obtener la dirección de envío
     * @return dirección
     */
    public String getAddress() {
        return address;
    }

    /**
     * Método para asignar la dirección
     * @param address dirección
     */
    public void setAddress(String address) {
        this.address = address;
    }
}
