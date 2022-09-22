package com.client.eurekaclient.models.DTO.JWT;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDHEncrypter;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;


@Service
public class JWEMicroserviceSecret {
    private ECKey ecKey;
    private final Curve currentCurve = Curve.P_521;
    private final JWEAlgorithm currentJWEAlgorithm = JWEAlgorithm.ECDH_ES_A256KW;
    private EncryptionMethod encryptionMethod = EncryptionMethod.A256CBC_HS512;
    private JWEHeader jweHeader;
    private static final Logger logger = LoggerFactory.getLogger(JWS.class);

    public JWEMicroserviceSecret() {
        try {
            this.ecKey = new ECKeyGenerator(this.currentCurve).generate();
        } catch (JOSEException e) {
            logger.error(e.getMessage());
        }
        jweHeader = new JWEHeader(currentJWEAlgorithm, encryptionMethod);
    }

    public String generateJWE(Object keyId) throws JOSEException {
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder().issuer("https://reborn.cash").audience("https://reborn.cash").claim("keyId", keyId).expirationTime(Date.from(Instant.now().plusSeconds(30 * 60))).build();
        JWEObject jweObject = new JWEObject(jweHeader, jwtClaimsSet.toPayload());
        try {
            jweObject.encrypt(new ECDHEncrypter(ecKey));
            System.out.printf("private: %s\n\n", Base64.getEncoder().encodeToString(ecKey.toECPrivateKey().getS().toByteArray()));
        } catch (JOSEException e) {
            logger.error(e.getMessage());
        }
        return jweObject.serialize();
    }
}
