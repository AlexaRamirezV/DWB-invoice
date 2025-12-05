package com.invoice.config.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * Configuración de seguridad para clientes Feign.
 * Esta clase se encarga de propagar el token de autorización entre
 * microservicios.
 * 
 */
@Configuration
public class FeignClientConfig {

    /**
     * Bean interceptor que se ejecuta antes de cada petición saliente de Feign.
     * Captura el token Bearer de la petición original entrante y lo inyecta
     * en la cabecera de la nueva petición interna.
     * 
     * @return RequestInterceptor configurado.
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                        .getRequestAttributes();
                if (attributes != null) {
                    // Obtener el header Authorization de la petición original (la de Postman)
                    String token = attributes.getRequest().getHeader("Authorization");
                    if (token != null) {
                        // Agregarlo a la petición saliente (hacia product-service o customer)
                        template.header("Authorization", token);
                    }
                }
            }
        };
    }
}
