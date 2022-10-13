package com.client.eurekaclient.services.authorization.filters;

import com.client.eurekaclient.messages.Errors;
import com.client.eurekaclient.models.DTO.JWT.JWS;
import com.client.eurekaclient.models.DTO.users.User;
import com.client.eurekaclient.services.authorization.UserDetailsServiceImplementation;
import com.client.eurekaclient.services.openfeign.key.KeyInterface;
import com.client.eurekaclient.services.openfeign.verify.VerifyInterface;
import com.client.eurekaclient.utilities.JWT.JWTUtility;
import com.nimbusds.jose.jwk.ECKey;
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
import javax.servlet.http.HttpServletRequestWrapper;
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
    private VerifyInterface verifyInterface;
    @Autowired
    private KeyInterface keyInterface;
    @Autowired
    private Errors errors;
    private JWS jws = new JWS(null);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (jws.getEcKey() == null) jws.setEcKey(ECKey.parse(this.keyInterface.getKey()));
            if (jwtUtility.getJws() == null) jwtUtility.setJws(this.jws);
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtility.validateJwtToken(jwt)) {
                String username = jwtUtility.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                Optional<User> optionalUser = userDetailsService.loadUser(username);
                if (optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.USER_NOT_FOUND);
                User user = optionalUser.get();
                if (user.getStatus().equalsIgnoreCase("banned")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.USER_BANNED);
                if (user.isTwoFA() && verifyInterface.isExistVerify(user.getUsername())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.NEED_TO_PASS_2FA);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                Date exp = Date.from(LocalDateTime.now().plusMinutes(30)
                        .atZone(ZoneId.systemDefault()).toInstant());
                response.setHeader("AccessToken", jws.generateJWSWithTime(username, exp));
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
