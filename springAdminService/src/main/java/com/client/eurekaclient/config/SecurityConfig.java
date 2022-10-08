package com.client.eurekaclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler =
                new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl("/");

        http.authorizeRequests()
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated().and()
                .formLogin().loginPage("/login")
                .successHandler(successHandler).and()
                .logout().logoutUrl("/logout").and()
                .httpBasic().and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringAntMatchers(
                        "/instances",
                        "/actuator/**"
                );
//        http
//                .authorizeRequests()
//                .antMatchers(this.adminServer.getContextPath() + "/assets/**").permitAll()
//                .antMatchers(this.adminServer.getContextPath() + "/login").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage(this.adminServer.getContextPath() + "/login")
//                .successHandler(successHandler)
//                .and()
//                .logout()
//                .logoutUrl(this.adminServer.getContextPath() + "/logout")
//                .and()
//                .httpBasic()
//                .and()
//                .csrf()
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .ignoringRequestMatchers(
//                        new AntPathRequestMatcher(this.adminServer.getContextPath() +
//                                "/instances", HttpMethod.POST.toString()),
//                        new AntPathRequestMatcher(this.adminServer.getContextPath() +
//                                "/instances/*", HttpMethod.DELETE.toString()),
//                        new AntPathRequestMatcher(this.adminServer.getContextPath() + "/actuator/**"))
//                .and()
//                .rememberMe()
//                .key(UUID.randomUUID().toString())
//                .tokenValiditySeconds(1209600);
        return http.build();
    }
}