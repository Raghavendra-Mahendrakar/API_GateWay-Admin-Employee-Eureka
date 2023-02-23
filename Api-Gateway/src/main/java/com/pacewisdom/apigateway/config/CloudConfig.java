package com.pacewisdom.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/admin/**")

                        .filters(f -> f.addRequestHeader("admin-request", "admin-request-header")
                                .addResponseHeader("admin-response", "admin-response-header"))
                        .uri("http://localhost:8080/**")
                        .id("ADMIN-SERVICE"))

                .route(r -> r.path("/employee/**")

                        .filters(f -> f.addRequestHeader("employee-request", "employee-request-header")
                                .addResponseHeader("employee-response", "employee-response-header"))
                        .uri("http://localhost:8081/**")
                        .id("EMPLOYEE_SERVICE"))
                .build();
    }

}
