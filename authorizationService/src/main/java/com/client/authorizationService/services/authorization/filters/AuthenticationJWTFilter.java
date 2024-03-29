package com.client.authorizationService.services.authorization.filters;

import com.client.authorizationService.errors.messages.Errors;
import com.client.authorizationService.services.authorization.UserDetailsServiceImplementation;
import com.client.authorizationService.models.DTO.users.User;
import com.client.authorizationService.utilities.JWT.JWTUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.Optional;

public class AuthenticationJWTFilter extends OncePerRequestFilter {
    @Autowired
    private JWTUtility jwtUtility;
    @Autowired
    private UserDetailsServiceImplementation userDetailsService;
    @Autowired
    private Errors errors;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationJWTFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtility.validateJwtToken(jwt)) {
                System.out.println(jwtUtility.getJws().getEcKey().toJSONObject());
                String username = jwtUtility.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                Optional<User> optionalUser = userDetailsService.loadUser(username);
                if (optionalUser.isEmpty())
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.USER_NOT_FOUND);
                if(optionalUser.get().getStatus().equalsIgnoreCase("banned"))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.USER_BANNED);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }
}
