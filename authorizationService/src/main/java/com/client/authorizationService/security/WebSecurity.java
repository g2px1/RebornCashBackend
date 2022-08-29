package com.client.authorizationService.security;

import com.client.authorizationService.services.authorization.UserDetailsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

@Configuration
@EnableWebSecurity
@EnableWebFluxSecurity
public class WebSecurity {
    @Autowired
    private UserDetailsServiceImplementation userDetailsServiceImplementation;

//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    @Bean
//    SecurityWebFilterChain apiHttpSecurity(ServerHttpSecurity http) {
//                http
//                .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/api/**"))
//                .authorizeExchange((exchanges) -> exchanges.anyExchange().authenticated())
//                .oauth2ResourceServer(OAuth2ResourceServerSpec::jwt);
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/**").permitAll();
        return http.build();
    }
    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        return userDetailsServiceImplementation;
    }
}
