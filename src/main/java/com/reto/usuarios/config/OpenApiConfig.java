package com.reto.usuarios.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio de Usuarios - Plazoleta")
                        .description("API para la gestión de usuarios, roles y autenticación del sistema de plazoleta de comidas")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Carlos Jesus Manyoma Murillo")
                                .email("carlos.manyoma@pragma.com.co")
                                ))
                .servers(List.of(
                        new Server().url("http://localhost:8081").description("Servidor Local")
                ));
    }
}
