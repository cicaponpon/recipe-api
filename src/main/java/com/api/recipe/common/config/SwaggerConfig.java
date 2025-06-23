package com.api.recipe.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI recipeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Recipe API")
                        .description("API for managing recipes")
                        .version("1.0")
                        .contact(new Contact().name("Chernhelyn Caponpon").email("chernhelyn29@gmail.com"))
                        .license(new License().name("MIT License").url("https://opensource.org/licenses/MIT")));

    }
}
