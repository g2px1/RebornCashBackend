package com.client.eurekaclient.models.DTO.JWT;

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
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JWS {
    private String keyId = UUID.randomUUID().toString();
    private volatile ECKey ecKey;
    private final Curve currentCurve = Curve.P_521;
    private final JWSAlgorithm currentJWSAlgorithm = JWSAlgorithm.ES512;
    private JWSHeader jwsHeader;
    private static final Logger logger = LoggerFactory.getLogger(JWS.class);

    public JWS() {
        try {
            this.ecKey = new ECKeyGenerator(this.currentCurve).keyID(this.keyId).generate();
        } catch (JOSEException e) {
            logger.error(e.getMessage());
        }
        jwsHeader = new JWSHeader.Builder(this.currentJWSAlgorithm).type(JOSEObjectType.JWT).keyID(this.keyId).build();
    }

    public String generateJWS(String username) {
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder().issuer("https://reborn.cash").jwtID(UUID.randomUUID().toString()).audience("https://reborn.cash").claim("username", username).expirationTime(Date.from(Instant.now().plusSeconds(30 * 60))).build();
        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
        try {
            signedJWT.sign(new ECDSASigner(this.ecKey));
        } catch (JOSEException e) {
            logger.error(e.getMessage());
        }
        return signedJWT.serialize();
    }


    public String generateJWSWithTime(String username, Date date) {
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder().issuer("https://reborn.cash").jwtID(UUID.randomUUID().toString()).audience("https://reborn.cash").claim("username", username).expirationTime(date).build();
        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
        try {
            signedJWT.sign(new ECDSASigner(this.ecKey));
        } catch (JOSEException e) {
            logger.error(e.getMessage());
        }
        return signedJWT.serialize();
    }

    public  boolean validateJWS(String jws) {
        try {
            JWSVerifier verifier = new ECDSAVerifier(this.ecKey.toECPublicKey());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public ECKey getEcKey() {
        return this.ecKey;
    }

    public JWSHeader getJwsHeader() {
        return jwsHeader;
    }

    public void setEcKey(ECKey ecKey) {
        this.ecKey = ecKey;
    }
}
