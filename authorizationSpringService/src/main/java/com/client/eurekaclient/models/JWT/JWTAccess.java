package com.client.eurekaclient.models.JWT;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class JWTAccess {
    private String keyId = UUID.randomUUID().toString();
    private ECKey ecKey;
    private final Curve currentCurve = Curve.P_521;
    private final JWSAlgorithm currentJWSAlgorithm = JWSAlgorithm.ES512;
    private JWSHeader jwsHeader;
    private static final Logger logger = LoggerFactory.getLogger(JWTAccess.class);

    public JWTAccess() {
        try {
            this.ecKey = new ECKeyGenerator(this.currentCurve).keyID(this.keyId).generate();
            System.out.printf("x: %s, y: %s, d: %s\n", ecKey.getX(), ecKey.getY(), ecKey.getD());
            System.out.printf("json: %s \n", ecKey.toJSONObject());
        } catch (JOSEException e) {
            logger.error(e.getMessage());
        }
        jwsHeader = new JWSHeader.Builder(this.currentJWSAlgorithm).type(JOSEObjectType.JWT).keyID(this.keyId).build();
    }

    public String generateJWS(String username) {
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder().issuer("https://reborn.cash").audience("https://reborn.cash").claim("username", username).expirationTime(Date.from(Instant.now().plusSeconds(30 * 60))).build();
        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
        try {
            signedJWT.sign(new ECDSASigner(this.ecKey));
        } catch (JOSEException e) {
            logger.error(e.getMessage());
        }
        return signedJWT.serialize();
    }

    public boolean validateJWS(String jws) throws JOSEException {
        JWSVerifier verifier = new ECDSAVerifier(this.ecKey.toECPublicKey());
        return true;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public JWSHeader getJwsHeader() {
        return jwsHeader;
    }
}
