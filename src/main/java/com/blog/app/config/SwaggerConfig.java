package com.blog.app.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Profile("dev")
    @Bean
    public OpenAPI devOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(securityRequirement())
                .servers(Arrays.asList(new Server().url("localhost:8080").description("Development Server")))
                .components(components());
    }

    @Profile("prod")
    @Bean
    public OpenAPI prodOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(securityRequirement())
                .servers(Arrays.asList(new Server().url("localhost:8080").description("Production Server")))
                .components(components());
    }

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("v1")
                .pathsToMatch("/**")
                .build();
    }

    private Info apiInfo() {
        return new Info()
                .title("Blogging Application APIs")
                .description("This project is created by Anushka to provide APIs for blogging app.")
                .version("v1.0")
                .termsOfService("Terms of Service")
                .contact(new Contact().name("Anushka").url("http://abc.com").email("anushka@gmail.com"))
                .license(new License().name("License").url("")); // Add URL for license if applicable
    }

    private Components components(){
        return new Components().addSecuritySchemes("JWT", securityScheme());
    }

    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement().addList("JWT");
    }

    private SecurityScheme securityScheme(){
        return new SecurityScheme()
                .name("JWT")
                .type(Type.HTTP)
                .scheme("Bearer")
                .bearerFormat("JWT");
    }
}
