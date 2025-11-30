package com.invoice.config.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeignClientConfig {

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
                        // Agregarlo a la petición saliente (hacia Product-Service)
                        template.header("Authorization", token);
                    }
                }
            }
        };
    }
}
