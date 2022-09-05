package com.client.authorizationService;

import com.client.authorizationService.utilities.random.Rnd;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.*;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class AuthorizationServiceApplication {
    public static void main(String[] args) throws JOSEException, BadJOSEException, ParseException, NoSuchAlgorithmException {
        String keyId = UUID.randomUUID().toString();
        System.out.println(keyId);
        ECKey ecKey = new ECKeyGenerator(Curve.P_256).keyID(keyId).generate();
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.ES256).type(JOSEObjectType.JWT).keyID(keyId).build();
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder().issuer("me").audience("you").subject("bob").claim("username", "g2px1").expirationTime(Date.from(Instant.now().plusSeconds(1000))).build();
        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
        signedJWT.sign(new ECDSASigner(ecKey));
        System.out.println(signedJWT.serialize());
        JWSVerifier verifier1 = new ECDSAVerifier(ecKey.toECPublicKey());
        System.out.printf("verifier1: %s \n\n", SignedJWT.parse(signedJWT.serialize()).verify(verifier1));

        SignedJWT signedJWT1 = SignedJWT.parse(signedJWT.serialize());
        ECKey ecKey1 = new ECKeyGenerator(Curve.P_256).algorithm(JWSAlgorithm.ES256).keyID(signedJWT1.getHeader().getKeyID()).generate();
        JWSVerifier verifier11 = new ECDSAVerifier(ECKey.parse(signedJWT.getSignature().toString()));
        System.out.printf("verifier parsed: %s\n\n", signedJWT1.verify(verifier11));


//        JWSVerifier verifier1 = new MACVerifier(keyId.getBytes());
//        JWSObject jwsObject1 = new JWSObject(jwsHeader, jwtClaimsSet.toPayload());
//        boolean verifiedSignature1 = false;
//        try {
//            verifiedSignature1 = signedJWT.verify(verifier1);
//        } catch (JOSEException e) {
//            System.err.println("Couldn't verify1 signature: " + e.getMessage());
//        }
//        if (verifiedSignature1) {
//            System.out.println("Verified JWS1 signature!");
//        }


        // Create payload
        String message = "Hello world!";
        Payload payload = new Payload(message);
        System.out.println("JWS payload message: " + message);
        // Create JWS header with HS256 algorithm
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        System.out.println("JWS header: " + header.toJSONObject());
        // Create JWS object
        JWSObject jwsObject = new JWSObject(header, payload);
        // Create HMAC signer
        String sharedKey = "a0a2abd8-6162-41c3-83d6-1cf559b46afc";
        System.out.println("HMAC key: " + sharedKey);
        JWSSigner signer = new MACSigner(sharedKey.getBytes());
        try {
            jwsObject.sign(signer);
        } catch (JOSEException e) {
            System.err.println("Couldn't sign JWS object: " + e.getMessage());
            return;
        }
        // Serialise JWS object to compact format
        String s = jwsObject.serialize();
        System.out.println("Serialised JWS object: " + s);
        // Parse back and check signature
        try {
            jwsObject = JWSObject.parse(s);
        } catch (ParseException e) {
            System.err.println("Couldn't parse JWS object: " + e.getMessage());
            return;
        }
        System.out.println("JWS object successfully parsed");
        JWSVerifier verifier = new MACVerifier(sharedKey.getBytes());
        boolean verifiedSignature = false;
        try {
            verifiedSignature = jwsObject.verify(verifier);
        } catch (JOSEException e) {
            System.err.println("Couldn't verify signature: " + e.getMessage());
        }
        if (verifiedSignature) {
            System.out.println("Verified JWS signature!");
        }
        else {
            System.out.println("Bad JWS signature!");
            return;
        }
        System.out.printf("Recovered payload message: %s\n\n\n\n", jwsObject.getPayload());


//        JWEAlgorithm alg = JWEAlgorithm.RSA_OAEP_256;
//        EncryptionMethod enc = EncryptionMethod.A128CBC_HS256;
//        KeyPairGenerator rsaGen = KeyPairGenerator.getInstance("RSA");
//        rsaGen.initialize(2048);
//        KeyPair rsaKeyPair = rsaGen.generateKeyPair();
//        RSAPublicKey rsaPublicKey = (RSAPublicKey)rsaKeyPair.getPublic();
//        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey)rsaKeyPair.getPrivate();
//        // Generate the Content Encryption Key (CEK)
//        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//        keyGenerator.init(enc.cekBitLength());
//        SecretKey cek = keyGenerator.generateKey();
//        // Encrypt the JWE with the RSA public key + specified AES CEK
//        JWEObject jwe = new JWEObject(
//                new JWEHeader(alg, enc),
//                new Payload("Hello, world!"));
//        jwe.encrypt(new RSAEncrypter(rsaPublicKey, cek));
//        String jweString = jwe.serialize();
//        System.out.printf("jwe: %s\n", jweString);
//        // Decrypt the JWE with the RSA private key
//        jwe = JWEObject.parse(jweString);
//        jwe.decrypt(new RSADecrypter(rsaPrivateKey));
//        // Decrypt JWE with CEK directly, with the DirectDecrypter in promiscuous mode
//        jwe = JWEObject.parse(jweString);
//        jwe.decrypt(new DirectDecrypter(cek, true));
//        System.out.println(jwe.getPayload().toString());
//        SpringApplication.run(AuthorizationServiceApplication.class, args);
    }
}
