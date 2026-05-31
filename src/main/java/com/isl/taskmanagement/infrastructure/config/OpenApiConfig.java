package com.isl.taskmanagement.infrastructure.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI taskManagementOpenAPI() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("Task Management REST API")
                                .version("v1")
                                .description(
                                        """
                                        Production-grade Task Management API.

                                        Features:
                                        - CRUD Operations
                                        - Pagination
                                        - Filtering
                                        - Sorting
                                        - Validation
                                        - Global Exception Handling
                                        - API Versioning
                                        - In-Memory Caching
                                        - Correlation ID Tracing
                                        - Micrometer Metrics
                                        - Prometheus Monitoring
                                        - Distributed Tracing
                                        - Zipkin Integration

                                        Versioned Endpoints:
                                        /api/v1/tasks
                                        """
                                )
                                .license(
                                        new License()
                                                .name("Apache 2.0")
                                )
                );
    }
}