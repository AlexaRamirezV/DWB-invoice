package com.invoice.commons.util;

import java.util.Base64;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invoice.exception.ApiException;

@Component
public class JwtDecoder {
	
	public boolean isAdmin() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	        if (authentication != null && authentication.isAuthenticated()) {
	            return authentication.getAuthorities()
	                .stream()
	                .anyMatch(authority -> "ADMIN".equals(authority.getAuthority()));
	        }

	        return false;
		}catch(Exception e) {
			System.out.println("El usuario no es administrador");
			return false;
		}
	}
	
	public Integer getUserId() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Map<String, Object> payload = (Map<String, Object>) authentication.getCredentials();
			return (Integer) payload.get("id");
		}catch(Exception e) {
			throw new ApiException(HttpStatus.PRECONDITION_FAILED, "El usuario es inválido");
		}
	}

	public Integer extractUserId(String token) {
        try {
            // El token viene como "header.payload.signature"
            String[] chunks = token.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();
            
            // Decodificamos el payload (el segundo bloque)
            String payload = new String(decoder.decode(chunks[1]));
            
            // Convertimos el JSON a objeto para sacar el ID
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(payload);
            
            // Obtenemos el "id" (asegúrate que tu Auth API guarde el claim como "id")
            return node.get("id").asInt();
            
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Token inválido o corrupto");
        }
    }
}
