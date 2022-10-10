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
                .build();
    }
    @Bean
    public RouteLocator articleRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/api/articles/save")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/articles/save", "/articleService/save");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://articleService/"))
                .route(p -> p
                        .path("/api/articles/update")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/articles/update", "/articleService/update");
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
                        .path("/api/oreHunt/securedMineService/createMine")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/oreHunt/securedMineService/createMine", "/oreChainService/securedMineService/createMine");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://oreHuntService/"))
                .route(p -> p
                        .path("/api/oreHunt/securedMineService/closeMine")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/oreHunt/securedMineService/closeMine", "/oreChainService/securedMineService/closeMine");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://oreHuntService/"))
                .route(p -> p
                        .path("/api/oreHunt/securedMineService/editMine")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/oreHunt/securedMineService/editMine", "/oreChainService/securedMineService/editMine");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://oreHuntService/"))
                .route(p -> p
                        .path("/api/oreHunt/securedMineService/loadMine")// returns one trap
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/oreHunt/securedMineService/loadMine", "/oreChainService/securedMineService/loadMine");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://oreHuntService/"))
                .route(p -> p
                        .path("/api/oreHunt/securedMineService/loadMines") // returns many traps
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/oreHunt/securedMineService/loadMines", "/oreChainService/securedMineService/loadMines");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://oreHuntService/"))
                .route(p -> p
                        .path("/api/oreHunt/securedMineService/getMinesImage/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/oreHunt/securedMineService/getMinesImage/(?<mineImage>.*)", "/oreChainService/securedMineService/getMinesImage/${mineImage}");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://oreHuntService/"))
                .route(p -> p
                        .path("/api/oreHunt/securedMineService/getMinesImages")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/oreHunt/securedMineService/getMinesImages", "/oreChainService/securedMineService/getMinesImages");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://oreHuntService/"))
                .route(p -> p
                        .path("/api/oreHunt/securedTxService/createToken")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/oreHunt/securedTxService/createToken", "/oreChainService/securedTxService/createToken");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://oreHuntService/"))
                .route(p -> p
                        .path("/api/oreHunt/securedTxService/loadTokens")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/oreHunt/securedTxService/loadTokens", "/oreChainService/securedTxService/loadTokens");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://oreHuntService/"))
                .route(p -> p
                        .path("/api/oreHunt/securedTxService/balanceOf/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/oreHunt/securedTxService/balanceOf/(?<address>.*)", "/oreChainService/securedTxService/balanceOf/${address}");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://oreHuntService/"))
                .route(p -> p
                        .path("/api/oreHunt/securedTxService/distributeToken")
                        .filters(f -> f.filter((exchange, chain) -> {
                                    ServerHttpRequest req = exchange.getRequest();
                                    addOriginalRequestUrl(exchange, req.getURI());
                                    String path = req.getURI().getRawPath();
                                    String newPath = path.replaceAll("/api/oreHunt/securedTxService/distributeToken", "/oreChainService/securedTxService/distributeToken");
                                    ServerHttpRequest request = req.mutate().path(newPath).build();
                                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                                    return chain.filter(exchange.mutate().request(request).build());
                                }))
                        .uri("lb://oreHuntService/"))
                .build();
    }
}
