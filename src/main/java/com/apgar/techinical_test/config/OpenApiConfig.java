package com.apgar.techinical_test.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().info(new Info()
                .title("APGAR - Technical Test")
                .description("API REST para gerenciamento de reservas de salas de reunião e estatísticas de ocupação.")
                .version("1.0"));
    }
}