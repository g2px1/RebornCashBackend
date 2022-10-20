package com.client.eurekaclient;

import com.nimbusds.jose.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.openfeign.EnableFeignClients;

import java.text.ParseException;

@SpringBootApplication
//@EnableFeignClients
public class EurekaClientApplication {
    public static void main(String[] args) throws JOSEException, ParseException {
        SpringApplication.run(EurekaClientApplication.class, args);
    }
}
