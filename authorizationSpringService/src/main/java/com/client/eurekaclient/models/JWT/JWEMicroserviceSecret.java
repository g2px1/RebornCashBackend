package com.client.eurekaclient.models.JWT;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDHEncrypter;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;


@Service
public class JWEMicroserviceSecret {
    @Value("${app.services.JWESecret}")
    private String keyId;
    @Value("${app.services.privateKey.file}")
    private String secretFile;
    private ECKey ecKey;
    private final Curve currentCurve = Curve.P_521;
    private final JWEAlgorithm currentJWEAlgorithm = JWEAlgorithm.ECDH_ES_A256KW;
    private EncryptionMethod encryptionMethod = EncryptionMethod.A256CBC_HS512;
    private JWEHeader jweHeader;
    private static final Logger logger = LoggerFactory.getLogger(JWTAccess.class);

    public JWEMicroserviceSecret() {
        try {
            this.ecKey = new ECKeyGenerator(this.currentCurve).generate();
            JcaPEMWriter writer = new JcaPEMWriter(new FileWriter(secretFile));
            writer.writeObject(Base64.getEncoder().encodeToString(ecKey.toECPrivateKey().getS().toByteArray()));
            writer.close();
        } catch (JOSEException | IOException e) {
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
