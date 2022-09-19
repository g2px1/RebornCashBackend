package com.client.eurekaclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.io.IOException;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class EurekaClientApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(EurekaClientApplication.class, args);
    }
}
