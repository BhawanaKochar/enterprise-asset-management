package com.bhawana.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;

@Configuration
public class GatewayConfig {

    @Bean
    RouterFunction<ServerResponse> gatewayRoutes() {

        return route("employee-service")
                .GET("/api/employees/**", http())
                .before(uri("http://employee-service:8081"))
                .build()

                .and(route("asset-service")
                        .GET("/api/assets/**", http())
                        .before(uri("http://asset-service:8082"))
                        .build());
    }
}