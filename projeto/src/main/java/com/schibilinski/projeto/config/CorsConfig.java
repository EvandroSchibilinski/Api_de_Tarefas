package com.schibilinski.projeto.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Libera o acesso à API para o frontend React rodando em um servidor
 * separado durante o desenvolvimento (Vite, porta 5173).
 *
 * Em produção, se o build do React (pasta dist) for copiado para
 * src/main/resources/static, o frontend passa a ser servido pelo próprio
 * Spring Boot e essa configuração deixa de ser estritamente necessária.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173", "http://127.0.0.1:5173")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
