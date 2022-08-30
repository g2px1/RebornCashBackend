package com.client.verifyService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class VerifyServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(VerifyServiceApplication.class, args);
    }
}
