package com.client.eurekaclient.configuration.router;

//import com.client.eurekaclient.configuration.router.filters.JWTValidationFilter;
import com.client.eurekaclient.errors.messages.ErrorMessage;
import com.client.eurekaclient.services.openfeign.AuthorizationInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Random;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

@Configuration
public class RouterConfiguration {
    private static final Random rnd = new Random();
    @Autowired
    private AuthorizationInterface authorizationInterface;
//    @Autowired
//    private JWTValidationFilter jwtValidationFilter;
//
//    @Bean
//    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route(p -> p
//                        .path("/test")
//                        .filters(f -> f.filter((exchange, chain) -> {
//                            ServerHttpRequest req = exchange.getRequest();
//                            addOriginalRequestUrl(exchange, req.getURI());
//                            String path = req.getURI().getRawPath();
//                            String newPath = path.replaceAll("/test", "/userService/test");
//                            ServerHttpRequest request = req.mutate().path(newPath).build();
//                            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
//                            return chain.filter(exchange.mutate().request(request).build());
//                        }).filter(((exchange, chain) -> {
//                            ServerHttpRequest req = exchange.getRequest();
//                            return chain.filter(exchange.mutate().request(req).build());
//                        })))
//                        .uri("lb://userService/"))
//                .build();
//    }
    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/api/authorize")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/authorize", "/authorizationService/user/authorizeUser");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://authorizationService/"))
                .build();
    }
    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/api/authorize")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/authorize", "/authorizationService/user/authorizeUser");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://authorizationService/"))
                .build();
    }
}
