package com.client.eurekaclient.controllers;

import com.client.eurekaclient.models.DTO.transactions.TransactionResult;
import com.client.eurekaclient.models.request.web3.TransactionRequest;
import com.client.eurekaclient.services.web3.Web3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/secureWEB3/")
public class SecuredWEB3 {
    @Autowired
    private Web3Service web3Service;

    @PostMapping("/sendStableCoins")
    public TransactionResult sendStableCoins(@RequestBody TransactionRequest transactionRequest) {
        return web3Service.sendSecuredStableCoinTransaction(transactionRequest.chainName, transactionRequest.amount, transactionRequest.username);
    }

    @PostMapping("/sendGame")
    public TransactionResult sendGame(@RequestBody TransactionRequest transactionRequest) {
        return web3Service.sendSecuredGameTransaction(transactionRequest.chainName, transactionRequest.amount, transactionRequest.username);
    }
}
