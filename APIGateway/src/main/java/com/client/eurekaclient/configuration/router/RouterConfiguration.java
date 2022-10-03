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
                        .path("/api/authentication/approveVerifyCode")
                        .filters(f -> f.filter((exchange, chain) -> {
                            ServerHttpRequest req = exchange.getRequest();
                            addOriginalRequestUrl(exchange, req.getURI());
                            String path = req.getURI().getRawPath();
                            String newPath = path.replaceAll("/api/authentication/approveVerifyCode", "/authorizationService/user/approveVerifyCode");
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
                        .path("/api/articles/page/")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/articles/page/", "/articleService/page");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://articleService/"))
                .route(p -> p
                        .path("/api/articles/findByUuid/")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/articles/findByUuid/", "/articleService/findByUuid");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://articleService/"))
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
    public RouteLocator balanceRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/api/user/balance/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/user/balance/(?<username>.*)", "/userService/balance/${username}");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://userService/"))
                .build();
    }
    @Bean
    public RouteLocator web3UserRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/api/web3/deposit")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/web3/deposit", "/web3BalanceService/depositGame/");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://balanceService/"))
                .route(p -> p
                        .path("/api/web3/withdraw")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/web3/withdrawGame", "/web3BalanceService/depositGame/withdrawGame");
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
    public RouteLocator renderRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/", "/");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://renderingService/"))
                .build();
    }
    @Bean
    public RouteLocator rabbitHuntRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/api/rabbithunt/converting/convertLayer1")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/rabbithunt/convertLayer1", "/rabbitHuntService/converting/convertLayer1");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://rabbitHuntService/"))
                .route(p -> p
                        .path("/api/rabbithunt/converting/investInBurger")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/rabbithunt/converting/investInBurger", "/rabbitHuntService/converting/investInBurger");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://rabbitHuntService/"))
                .route(p -> p
                        .path("/api/rabbithunt/market/sellTokens")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/rabbithunt/market/sellTokens", "/rabbitHuntService/market/sellTokens");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://rabbitHuntService/"))
                .route(p -> p
                        .path("/api/rabbithunt/market/buyTokens")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/rabbithunt/market/buyTokens", "/rabbitHuntService/market/buyTokens");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://rabbitHuntService/"))
                .route(p -> p
                        .path("/api/rabbithunt/market/sellCellsPack")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/rabbithunt/market/sellCellsPack", "/rabbitHuntService/market/sellCellsPack");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://rabbitHuntService/"))
                .route(p -> p
                        .path("/api/rabbithunt/market/buyCellsPack/")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/rabbithunt/market/buyCellsPack", "/rabbitHuntService/market/buyCellsPack");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://rabbitHuntService/"))
                .route(p -> p
                        .path("/api/rabbithunt/market/withdrawCellsPacks/")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/rabbithunt/market/withdrawCellsPacks", "/rabbitHuntService/market/withdrawCellsPacks");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://rabbitHuntService/"))
                .route(p -> p
                        .path("/api/rabbithunt/traps/loadTraps/")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/rabbithunt/traps/loadTraps/", "/rabbitHuntService/traps/loadTraps/");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://rabbitHuntService/"))
                .route(p -> p
                        .path("/api/rabbithunt/traps/buyTrapCells/")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/rabbithunt/traps/buyTrapCells/", "/rabbitHuntService/traps/buyTrapCells/");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://rabbitHuntService/"))
                .route(p -> p
                        .path("/api/rabbithunt/traps/loadTraps/")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/rabbithunt/traps/loadTraps/", "/rabbitHuntService/traps/loadTraps/");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://rabbitHuntService/"))
                .route(p -> p
                        .path("/api/rabbithunt/traps/loadTraps/")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/rabbithunt/traps/loadTraps/", "/rabbitHuntService/traps/loadTraps/");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://rabbitHuntService/"))
                .build();
    }
}
