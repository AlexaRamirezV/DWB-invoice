package com.invoice.commons.dto;

/**
 * Clase envoltorio genérica para las respuestas HTTP de la API.
 * 
 * Se utiliza para devolver mensajes informativos de éxito o detalles de error
 */
public class ApiResponse {

	private String message;

	/**
	 * Constructor
	 */
	public ApiResponse() {
		
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 */
	public ApiResponse(String message) {
		this.message = message;
	}

	/**
	 * Método para obtener el mensaje
	 * 
	 * @return mensaje
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Método para asignar un mensaje
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
