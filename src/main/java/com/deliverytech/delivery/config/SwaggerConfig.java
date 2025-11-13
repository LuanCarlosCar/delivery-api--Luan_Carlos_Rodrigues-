package com.deliverytech.delivery.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Delivery API")
                        .description("API para sistema de delivery de comida")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("DeliveryTech Team")
                                .email("contato@deliverytech.com")));
    }
}