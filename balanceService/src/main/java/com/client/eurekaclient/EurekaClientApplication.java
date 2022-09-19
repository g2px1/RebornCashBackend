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
//        StandardContractProvider standardContractProvider = new StandardContractProvider("https://smartbch.greyh.at/", "53d8e4408658f87a690bd033d95881f081de1a3fe355787081eb024ba30baf12");
//        Transaction transaction = standardContractProvider.getWeb3j().ethGetTransactionByHash("0x6eb68f49e508319e370a7f0a758a2ddeb6b26c81bc00673d7076f8f129b2b0c3").send().getTransaction().get();
//        System.out.println(BigDecimal.valueOf(new BigInteger(transaction.getInput().substring(74), 16).doubleValue()).divide(BigDecimal.valueOf(Math.pow(10, 18))));
//        System.out.println(new BigDecimal(new BigInteger(transaction.getInput().substring(74), 16)).divide(BigDecimal.valueOf(Math.pow(10, 18))));
        SpringApplication.run(EurekaClientApplication.class, args);
    }
}
