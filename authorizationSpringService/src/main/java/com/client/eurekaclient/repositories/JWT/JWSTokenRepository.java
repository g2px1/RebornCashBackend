package com.client.eurekaclient.repositories.JWT;

import com.client.eurekaclient.models.JWT.JWTAccess;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Repository
public class JWSTokenRepository implements CsrfTokenRepository {
    private JWTAccess jwtAccess = new JWTAccess();
    private String username;

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        return new DefaultCsrfToken("x-csrf-token", "_csrf", jwtAccess.generateJWS());
    }

    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        return null;
    }
}
