package com.invoice.api.dto;

/**
 * DTO de entrada para el proceso de finalización de compra.
 * Clase utilizada para la implementación de puntos extra:
 * - Información de pago
 * - Cupones de descuento
 */
public class DtoInvoiceIn {

    private String payment_method;
    private String card_number;
    private String coupon_code;

    /**
     * Constructor
     */
    public DtoInvoiceIn() {
    }

    /**
     * Método que obtiene el método de pago
     * @return método de pago
     */
    public String getPayment_method() {
        return payment_method;
    }

    /**
     * Método para asignar el método de pago
     * @param payment_method método de pago
     */
    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    /**
     * Método que obtiene el número de la tarjeta de crédito
     * @return número de tarjeta de crédito
     */
    public String getCard_number() {
        return card_number;
    }

    /**
     * Método para asignar el número de tarjeta
     * @param card_number número de tarjeta
     */
    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    /**
     * Método para obtener el código de cupón
     * @return código del cupón
     */
    public String getCoupon_code() {
        return coupon_code;
    }

    /**
     * Método para asignar el código de cupón
     * @param coupon_code código de cupón
     */
    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }
}
