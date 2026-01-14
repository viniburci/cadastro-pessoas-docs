package com.doban.cadastro_pessoas_docs.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.type.format.jackson.JacksonJsonFormatMapper;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * Configuração do Hibernate para usar o ObjectMapper do Spring para JSON
 */
@Configuration
public class HibernateConfig {

    @PostConstruct
    public void init() {
        System.out.println("✅ HibernateConfig carregado!");
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(ObjectMapper objectMapper) {
        System.out.println("✅ HibernatePropertiesCustomizer configurado com ObjectMapper: " + objectMapper);
        return (properties) -> {
            System.out.println("✅ Aplicando json_format_mapper ao Hibernate");
            properties.put(
                "hibernate.type.json_format_mapper",
                new JacksonJsonFormatMapper(objectMapper)
            );
        };
    }
}
