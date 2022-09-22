package com.client.eurekaclient.utilities.JWT;

import com.client.eurekaclient.models.JWT.JWS;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class JWTUtility {
    private volatile JWS jws = null;
    private static final Logger logger = LoggerFactory.getLogger(JWTUtility.class);

    public String getUserNameFromJwtToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getClaim("username").toString();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "not found";
        }
    }

    public boolean validateJwtToken(String authorizationToken) {
        try {
            JWSVerifier jwsVerifier = new ECDSAVerifier(jws.getEcKey());
            JWSObject jwsObject = JWSObject.parse(authorizationToken);
            return jwsObject.verify(jwsVerifier);
        } catch (JOSEException | ParseException ex) {
            logger.error("Invalid JWT signature: {}", ex.getMessage());
        }
        return false;
    }

    public JWS getJws() {
        return jws;
    }

    public void setJws(JWS jws) {
        this.jws = jws;
    }
}
