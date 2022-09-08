package com.client.eurekaclient;

import com.client.eurekaclient.models.JWT.JWEMicroserviceSecret;
import com.client.eurekaclient.models.JWT.JWTAccess;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDHEncrypter;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class EurekaClientApplication {
    public static void main(String[] args) throws JOSEException, ParseException {
//        JWTAccess jwtAccess = new JWTAccess();
//        System.out.println(jwtAccess.generateJWS("g2px1"));
        ECKey ecKey = ECKey.parse("""
                {"kty":"EC", "d":"Affb-CiELZeoGhlMu4mYSNecchSU9_Rl7l_gs-EMbb-w8POR7Gd_AE-t0g-Xly2QiDYeOKHVoJbTaP-dbBkfaWdo", "crv":"P-521", "kid":"a878607b-e5e5-4ab7-9ba5-f372c4a5ae1e", "x":"AC5-z_jTrCzxoxfRYHnE1sqkPpOFik3X375rw7NjJlR_1UpU0-ds3VtE2bdy4VQxZyaTmeYsW6w5XHNDb0l2OU62", "y":"AJ1fRMK6iyjjP5MiT303HOJShgbX17P8XVeLCrzsEKsM5uRgZsrdYEXSLduKV5Fdnuaff8XkZkaxQKLOecDLvvE_"}
                """);
        JWSObject jwsObject = JWSObject.parse("eyJraWQiOiJhODc4NjA3Yi1lNWU1LTRhYjctOWJhNS1mMzcyYzRhNWFlMWUiLCJ0eXAiOiJKV1QiLCJhbGciOiJFUzUxMiJ9.eyJpc3MiOiJodHRwczovL3JlYm9ybi5jYXNoIiwiYXVkIjoiaHR0cHM6Ly9yZWJvcm4uY2FzaCIsImV4cCI6MTY2MjY1MzQ2MCwidXNlcm5hbWUiOiJnMnB4MSJ9.ANPxEQyInVSJHfagJzW95YVIiCv2lwwOW3Ipt7Fh_tdyFDzzu3d1pQhmieyjJE2ctCmnI38vSFeIvfixcIu9GTZkAE2WDjdmKseKNKvQmsP8P9APL_84ajPDb-o1GJZBrAZZSocQ-sFqIq_n-JIwwnZgvH_U1yrjXh2RwN2gXuWR0JV0");
        JWSVerifier jwsVerifier = new ECDSAVerifier(ecKey);
        System.out.println(jwsObject.verify(jwsVerifier));
//        SpringApplication.run(EurekaClientApplication.class, args);
    }
}
