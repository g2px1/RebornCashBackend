package com.client.authorizationService.security;

import com.client.authorizationService.services.authorization.UserDetailsServiceImplementation;
import com.client.authorizationService.services.authorization.filters.AuthenticationJWTFilter;
import com.client.authorizationService.utilities.JWT.AuthenticationEntryPointJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity {
    @Autowired
    private UserDetailsServiceImplementation userDetailsServiceImplementation;
    @Autowired
    private AuthenticationEntryPointJWT unauthorizedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .authenticationProvider(authenticationProvider())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated();
        return http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class).build();
    }
    @Bean
    public AuthenticationJWTFilter authenticationJwtTokenFilter() { return new AuthenticationJWTFilter(); }
    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsServiceImplementation;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception { return authenticationConfiguration.getAuthenticationManager(); }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
