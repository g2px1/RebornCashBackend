package com.client.eurekaclient;

import com.client.eurekaclient.services.web3.gas.GasProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class EurekaClientApplication {
    public static void main(String[] args) throws IOException {
        GasProvider gasProvider = new GasProvider("https://bsc-dataseed.binance.org/");
        Optional<TransactionReceipt> transReceipt = gasProvider.getWeb3j().ethGetTransactionReceipt("0xc090c7a187c249e318f630f868dd00d859b4941951a59d2e2550ec8b455a6657").send().getTransactionReceipt();
        assert transReceipt.isPresent();
        System.out.println(transReceipt.get().getType());
        Optional<Transaction> optionalTransaction = gasProvider.getWeb3j().ethGetTransactionByHash("0x111784e99f6ffed7b5ac11a3a81438df6daeee1f52c2dffd257ede6a2e53f91f").send().getTransaction();
        assert optionalTransaction.isPresent();
        BigDecimal bigIntegerAmount = new BigDecimal( optionalTransaction.get().getValue()).divide(BigDecimal.valueOf(1_000_000_000_000_000_000L));
        System.out.println(optionalTransaction.get().getValue());
        System.out.printf("sum: %s\n", bigIntegerAmount);
//        SpringApplication.run(EurekaClientApplication.class, args);
    }
}
