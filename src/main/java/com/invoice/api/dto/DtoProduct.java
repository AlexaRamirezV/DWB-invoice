package com.invoice.api.dto;

/**
 * DTO que representa la información de un producto obtenida desde el product-service.
 * Se utiliza para validar la existencia del producto, consultar el stock
 * disponible en tiempo real y obtener el precio unitario vigente para los cálculos de la factura.
 */
public class DtoProduct {
    private String gtin;
    private Integer stock;
    private Double price;
    private String product;
    private String description;

    /**
     * Constructor
     */
    public DtoProduct() {
    }

    // Getters y Setters
    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
