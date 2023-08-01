package com.vti.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.List;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        Info apiInfo = new Info()
                .title("Department Manager")
                .description("Final Exam Advanced Java")
                .version("1.0.0")
                .contact(new Contact().email("quanganhdoan1299.vn@gmail.com")
                        .name("Quang Anh"));
                SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
        List<SecurityRequirement> securityRequirements = List.of(new SecurityRequirement().addList("bearerAuth"));

        return new OpenAPI().components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                            .info(apiInfo)
                            .security(securityRequirements);
    }

    @Bean
    public OperationCustomizer addMethodToPathSummary() {
        return (operation, handlerMethod) -> {
            if (handlerMethod instanceof HandlerMethod) {
                String methodName = ((HandlerMethod) handlerMethod).getMethod().getName();
                String currentSummary = operation.getSummary();
                if (currentSummary != null && !currentSummary.trim().isEmpty()) {
                    operation.summary(methodName + ": " + currentSummary);
                } else {
                    operation.summary(methodName);
                }
            }
            return operation;
        };
    }
}
