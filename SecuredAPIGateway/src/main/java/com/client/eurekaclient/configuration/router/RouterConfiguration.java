package com.client.eurekaclient.configuration.router;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

@Configuration
public class RouterConfiguration {
    @Bean
    public RouteLocator authorizationRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/api/authentication/authorize")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/authentication/authorize", "/authorizationService/user/authorizeUser");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://authorizationService/"))
                .route(p -> p
                        .path("/api/authentication/signup")
                        .filters(f -> f.filter((exchange, chain) -> {
                            ServerHttpRequest req = exchange.getRequest();
                            addOriginalRequestUrl(exchange, req.getURI());
                            String path = req.getURI().getRawPath();
                            String newPath = path.replaceAll("/api/authentication/signup", "/authorizationService/user/registerUser");
                            ServerHttpRequest request = req.mutate().path(newPath).build();
                            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                            return chain.filter(exchange.mutate().request(request).build());
                        }))
                        .uri("lb://authorizationService/"))
                .route(p -> p
                        .path("/api/authentication/resetPassword")
                        .filters(f -> f.filter((exchange, chain) -> {
                            ServerHttpRequest req = exchange.getRequest();
                            addOriginalRequestUrl(exchange, req.getURI());
                            String path = req.getURI().getRawPath();
                            String newPath = path.replaceAll("/api/authentication/resetPassword", "/authorizationService/user/resetPassword");
                            ServerHttpRequest request = req.mutate().path(newPath).build();
                            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                            return chain.filter(exchange.mutate().request(request).build());
                        }))
                        .uri("lb://authorizationService/"))
                .build();
    }
    @Bean
    public RouteLocator articleRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/api/articles/save/")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/articles/save/", "/articleService/save");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://articleService/"))
                .route(p -> p
                        .path("/api/articles/update/")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/articles/update/", "/articleService/update");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://articleService/"))
                .build();
    }
    @Bean
    public RouteLocator nftRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/api/nft/page/")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/nft/page/", "/nftService/page");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://nftService/"))
                .route(p -> p
                        .path("/api/nft/findByUuid/")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/nft/findByUuid/", "/nftService/findByUuid");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://nftService/"))
                .route(p -> p
                        .path("/api/nft/findNftByName/")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/nft/save/", "/nftService/findNftByName");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://nftService/"))
                .route(p -> p
                        .path("/api/nft/findByIndex/")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/nft/findByIndex/", "/nftService/findByIndex");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://nftService/"))
                .build();
    }
    @Bean
    public RouteLocator marketRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/api/market/page/")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/articles/page/", "/marketController/page");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://marketplaceService/"))
                .route(p -> p
                        .path("/api/market/findByUuid/")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/market/findByUuid/", "/marketController/findByUuid");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://marketplaceService/"))
                .build();
    }
    @Bean
    public RouteLocator secureWeb3UserRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/api/secureWEB3/addBlockchain")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/secureWEB3/addBlockchain", "/secureWEB3/addBlockchain");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://balanceService/"))
                .route(p -> p
                        .path("/api/secureWEB3/changeBlockchain")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/secureWEB3/changeBlockchain", "/secureWEB3/changeBlockchain");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://balanceService/"))
                .route(p -> p
                        .path("/api/secureWEB3/validateParsing")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/secureWEB3/validateParsing", "/secureWEB3/validateParsing");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://balanceService/"))
                .route(p -> p
                        .path("/api/secureWEB3/getBlockchainData/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/secureWEB3/changeBlockchain/(?<chainName>.*)", "/api/secureWEB3/changeBlockchain/${chainName}");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://balanceService/"))
                .build();
    }
    @Bean
    public RouteLocator unitUserRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/api/unit/balance/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/unit/balance/(?<address>.*)", "/unitBalanceService/getBalance/${address}");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://balanceService/"))
                .build();
    }
    @Bean
    public RouteLocator securedRenderRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/", "/adminPanel");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://renderingService/"))
                .build();
    }
    @Bean
    public RouteLocator securedRabbitHuntRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/api/rabbithunt/securedTrapService/createTrap")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/rabbithunt/securedTrapService/createTrap", "/rabbitHuntService/securedTrapService/createTrap");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://rabbitHuntService/"))
                .route(p -> p
                        .path("/api/rabbithunt/securedTrapService/closeTrap")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/rabbithunt/securedTrapService/closeTrap", "/rabbitHuntService/securedTrapService/closeTrap");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://rabbitHuntService/"))
                .route(p -> p
                        .path("/api/rabbithunt/securedTrapService/editTrap")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/rabbithunt/securedTrapService/editTrap", "/rabbitHuntService/securedTrapService/editTrap");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://rabbitHuntService/"))
                .route(p -> p
                        .path("/api/rabbithunt/securedTrapService/closeTrap")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/rabbithunt/securedTrapService/closeTrap", "/rabbitHuntService/securedTrapService/closeTrap");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://rabbitHuntService/"))
                .route(p -> p
                        .path("/api/rabbithunt/securedTrapService/loadTrap")// returns one trap
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/rabbithunt/securedTrapService/loadTrap", "/rabbitHuntService/securedTrapService/loadTrap");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://rabbitHuntService/"))
                .route(p -> p
                        .path("/api/rabbithunt/securedTrapService/loadTraps") // returns many traps
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/rabbithunt/securedTrapService/loadTraps", "/rabbitHuntService/securedTrapService/loadTraps");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://rabbitHuntService/"))
                .route(p -> p
                        .path("/api/rabbithunt/securedTrapService/getTrapsImage/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/rabbithunt/securedTrapService/getTrapsImage/(?<trapImage>.*)", "/rabbitHuntService/securedTrapService/getTrapsImage/${trapImage}");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://rabbitHuntService/"))
                .route(p -> p
                        .path("/api/rabbithunt/securedTrapService/getTrapsImages")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/rabbithunt/securedTrapService/getTrapsImages", "/rabbitHuntService/securedTrapService/getTrapsImages");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://rabbitHuntService/"))
                .route(p -> p
                        .path("/api/rabbithunt/securedTxService/createToken")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/rabbithunt/securedTxService/createToken", "/rabbitHuntService/securedTxService/createToken");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://rabbitHuntService/"))
                .route(p -> p
                        .path("/api/rabbithunt/securedTxService/loadTokens")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/rabbithunt/securedTxService/loadTokens", "/rabbitHuntService/securedTxService/loadTokens");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://rabbitHuntService/"))
                .route(p -> p
                        .path("/api/rabbithunt/securedTxService/balanceOf/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/rabbithunt/securedTxService/balanceOf/(?<address>.*)", "/rabbitHuntService/securedTxService/balanceOf/${address}");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://rabbitHuntService/"))
                .route(p -> p
                        .path("/api/rabbithunt/securedTxService/distributeToken")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/rabbithunt/securedTxService/distributeToken", "/rabbitHuntService/securedTxService/distributeToken");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://rabbitHuntService/"))
                .build();
    }
}
