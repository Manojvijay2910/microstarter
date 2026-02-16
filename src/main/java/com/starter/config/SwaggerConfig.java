package com.starter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
	
	@Value("${api.base-path:/api/v1}")
    private String basePath;

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Deploy-Ready Backend Template API")
                        .version("1.0")
                        .description("Production-ready Spring Boot Backend Base")
                        .contact(new Contact()
                                .name("Manoj Vijayvergiya")
                                .email("manojvijay29@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")))
                .servers(List.of(
                        new Server().url(basePath)
                ));
    }
}
