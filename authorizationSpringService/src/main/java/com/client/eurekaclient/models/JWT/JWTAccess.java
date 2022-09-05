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
    private String keyId;
    private ECKey ecKey;
    private final Curve currentCurve = Curve.P_521;
    private final JWSAlgorithm currentJWSAlgorithm = JWSAlgorithm.ES512;
    private JWSHeader jwsHeader;
    private JWTClaimsSet jwtClaimsSet;
    private SignedJWT signedJWT;
    private static final Logger logger = LoggerFactory.getLogger(JWTAccess.class);

    public JWTAccess(String username) {
        this.keyId = UUID.randomUUID().toString();
        try {
            this.ecKey = new ECKeyGenerator(this.currentCurve).keyID(this.keyId).generate();
        } catch (JOSEException e) {
            logger.error(e.getMessage());
        }
        jwsHeader = new JWSHeader.Builder(this.currentJWSAlgorithm).type(JOSEObjectType.JWT).keyID(this.keyId).build();
        new JWTClaimsSet.Builder().issuer("https://reborn.cash").audience("https://reborn.cash").claim("username", username).expirationTime(Date.from(Instant.now().plusSeconds(0))).build();
        signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
        try {
            this.signedJWT.sign(new ECDSASigner(ecKey));
        } catch (JOSEException e) {
            logger.error(e.getMessage());
        }
    }

    public String getJWS() {
        return signedJWT.serialize();
    }

    public static boolean validateJWS(String jws) {
        JWSVerifier verifier = new ECDSAVerifier(this.ecKey.toECPublicKey());
        return true;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public JWSHeader getJwsHeader() {
        return jwsHeader;
    }

    public void setJwsHeader(JWSHeader jwsHeader) {
        this.jwsHeader = jwsHeader;
    }

    public JWTClaimsSet getJwtClaimsSet() {
        return jwtClaimsSet;
    }

    public void setJwtClaimsSet(JWTClaimsSet jwtClaimsSet) {
        this.jwtClaimsSet = jwtClaimsSet;
    }

    public SignedJWT getSignedJWT() {
        return signedJWT;
    }

    public void setSignedJWT(SignedJWT signedJWT) {
        this.signedJWT = signedJWT;
    }
}
