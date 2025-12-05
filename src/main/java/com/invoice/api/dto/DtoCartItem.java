package com.invoice.api.dto;

/**
 * Clase DtoCartItem
 * Representa un artículo del carrito de compras.
 * 
 * Se utiliza para responder al cliente con el detalle completo del producto
 * (nombre, descripción, precio actual) obtenido desde el microservicio de Productos,
 * junto con la cantidad y el total calculado.
 */
public class DtoCartItem {

    private String gtin;
    private String product; // Nombre del producto
    private Double unit_price;
    private Integer quantity;
    private Double total; // Precio * Cantidad
    private String description;

    /**
     * Constructor
     */
    public DtoCartItem() {
    }

    /**
     * Método para obtener el gtin de un producto
     * @return gtin
     */
    public String getGtin() {
        return gtin;
    }

    /**
     * Método para asignar un gtin a un producto
     * @param gtin gtin de un producto
     */
    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    /**
     * Método para obtener el nombre de un producto
     * @return nombre del producto
     */
    public String getProduct() {
        return product;
    }

    /**
     * Método para asignar el nombre de un producto
     * @param product nombre del producto
     */
    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * Método para obtener el precio del producto
     * @return precio del producto
     */
    public Double getUnit_price() {
        return unit_price;
    }

    /**
     * Método para asignar el precio a un producto
     * @param unit_price precio
     */
    public void setUnit_price(Double unit_price) {
        this.unit_price = unit_price;
    }

    /**
     * Método para obtener la cantidad de artículos de un producto
     * @return cantidad de artículos
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Método para asignar la cantidad de artículos de un producto
     * @param quantity cantidad
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Método para obtener el total (precio * cantidad
     * @return total 
     */
    public Double getTotal() {
        return total;
    }

    /**
     * Método para asignar el total
     * @param total total
     */
    public void setTotal(Double total) {
        this.total = total;
    }

    /**
     * Método para obtener la descripción de un producto
     * @return descripción
     */
    public String getDescription() {
        return description;
    }

    /**
     * Método para asignar la descripción a un producto
     * @param description descripción
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
