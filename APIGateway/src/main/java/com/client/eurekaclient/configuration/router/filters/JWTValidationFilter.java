package com.client.eurekaclient.configuration.router.filters;

import com.client.eurekaclient.errors.messages.ErrorMessage;
import com.client.eurekaclient.services.openfeign.AuthorizationInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Service
public class JWTValidationFilter implements GatewayFilter, Ordered {
    @Autowired
    private AuthorizationInterface authorizationInterface;
    @Value("${app.urls.excluded}")
    private List<String> excludedUrls;
    private final Predicate<ServerHttpRequest> isSecured = request -> excludedUrls.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
        if (!httpHeaders.containsKey("Authorization") || Objects.requireNonNull(httpHeaders.get("Authorization")).isEmpty())
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.BEARER_REQUIRED));
        if (!authorizationInterface.validateToken(Objects.requireNonNull(httpHeaders.get("Authorization")).get(0)))
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.TOKEN_EXPIRED));
//        if (isSecured)
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
