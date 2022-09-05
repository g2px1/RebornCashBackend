package com.client.eurekaclient;

import com.client.eurekaclient.models.JWT.JWEMicroserviceSecret;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.ECDHEncrypter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.text.ParseException;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class EurekaClientApplication {
    public static void main(String[] args) throws JOSEException, ParseException {
        JWEMicroserviceSecret jweMicroserviceSecret = new JWEMicroserviceSecret();
//        JWEObject jweObject = JWEObject.parse(jweMicroserviceSecret.generateJWE("123"));
        System.out.println(jweMicroserviceSecret.generateJWE("123"));
//        SpringApplication.run(EurekaClientApplication.class, args);
    }
}
