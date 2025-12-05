package com.invoice.api.dto;

/**
 * DTO de salida para el listado de facturas.
 * Representa una vista resumida de una factura generada, incluyendo los
 * totales, impuestos, fecha de creación, dirección de envío y desglose de descuentos.
 */
public class DtoInvoiceList {
	
	private Integer id;
	
	private Integer user_id;
		
	private String created_at;
	
	private Double subtotal;
	
	private Double taxes;
	
	private Double total;

	private String shipping_address;

	private String payment_method;

	private String card_number;

	private String coupon_code;

	private Double discount;
	
	/**
	 * Constructor
	 */
	public DtoInvoiceList() {
		
	}

	/**
	 * Constructor
	 * @param id
	 * @param user_id
	 * @param created_at
	 * @param subtotal
	 * @param taxes
	 * @param total
	 * @param shipping_address
	 * @param payment_method
	 * @param card_number
	 * @param coupon_code
	 * @param discount
	 */
	public DtoInvoiceList(Integer id, Integer user_id, String created_at, Double subtotal, Double taxes, Double total, String shipping_address, 
			String payment_method, String card_number, String coupon_code, Double discount) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.created_at = created_at;
		this.subtotal = subtotal;
		this.taxes = taxes;
		this.total = total;
		this.shipping_address = shipping_address;
		this.payment_method = payment_method;
		this.card_number = card_number;
		this.coupon_code = coupon_code;
		this.discount = discount;
	}

	// Getters y setters

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public Double getTaxes() {
		return taxes;
	}

	public void setTaxes(Double taxes) {
		this.taxes = taxes;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	/**
	 * Método para obtener la dirección de envío
	 * @return
	 */
	public String getShipping_address() {
		return shipping_address;
	}

	/**
	 * Método para asignar la dirección de envío
	 * @param shipping_address
	 */
	public void setShipping_address(String shipping_address) {
		this.shipping_address = shipping_address;
	}

	/**
	 * Método para obtener el método de pago
	 * @return
	 */
	public String getPayment_method() {
		return payment_method;
	}

	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}

	public String getCard_number() {
		return card_number;
	}

	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}

	public String getCoupon_code() {
		return coupon_code;
	}

	public void setCoupon_code(String coupon_code) {
		this.coupon_code = coupon_code;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	
	
}
