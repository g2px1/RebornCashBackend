package com.client.eurekaclient;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.ECKey;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.ParseException;

@SpringBootApplication
public class EurekaClientApplication {
    public static void main(String[] args) throws JOSEException, ParseException {
        SpringApplication.run(EurekaClientApplication.class, args);
    }
}
