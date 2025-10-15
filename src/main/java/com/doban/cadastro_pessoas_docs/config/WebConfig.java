package com.doban.cadastro_pessoas_docs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**") // permite para todos endpoints
            .allowedOrigins("http://localhost:4200") // seu frontend Angular
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // métodos HTTP permitidos
            .allowedHeaders("*") // permite todos headers
            .allowCredentials(true);
  }
}