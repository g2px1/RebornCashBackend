package com.client.authorizationService.services.authorization.filters;

import com.client.authorizationService.errors.messages.ErrorMessage;
import com.client.authorizationService.models.JWT.JWS;
import com.client.authorizationService.services.authorization.UserDetailsServiceImplementation;
import com.client.authorizationService.models.DTO.users.User;
import com.client.authorizationService.utilities.JWT.JWTUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class AuthenticationJWTFilter extends OncePerRequestFilter {
    @Autowired
    private JWTUtility jwtUtility;
    @Autowired
    private UserDetailsServiceImplementation userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationJWTFilter.class);
    @Autowired
    private JWS jws;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtility.validateJwtToken(jwt)) {
                String username = jwtUtility.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                Optional<User> optionalUser = userDetailsService.loadUser(username);
                if (optionalUser.isEmpty())
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.USER_NOT_FOUND);
                if(optionalUser.get().getStatus().equalsIgnoreCase("banned"))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.USER_BANNED);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                Date exp = Date.from(LocalDateTime.now().plusMinutes(30)
                        .atZone(ZoneId.systemDefault()).toInstant());
                response.setHeader("Authorization", String.format("Bearer %s", jws.generateJWSWithTime(username, exp)));
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
