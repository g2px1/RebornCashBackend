package com.client.eurekaclient.configuration.router.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

public class UrlChangerUrlFilter implements GatewayFilter, Ordered {
    private String originalLink;
    private String newLink;
    private Logger logger = LoggerFactory.getLogger(UrlChangerUrlFilter.class);

    public UrlChangerUrlFilter(String originalLink, String newLink) {
        this.originalLink = originalLink;
        this.newLink = newLink;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest req = exchange.getRequest();
        logger.debug(req.toString());
        addOriginalRequestUrl(exchange, req.getURI());
        String path = req.getURI().getRawPath();
        String newPath = path.replaceAll(originalLink, newLink);
        ServerHttpRequest request = req.mutate().path(newPath).build();
        exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
