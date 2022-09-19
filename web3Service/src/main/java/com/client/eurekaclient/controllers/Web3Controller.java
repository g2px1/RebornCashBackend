package com.client.eurekaclient.controllers;

import com.client.eurekaclient.models.request.TransactionRequest;
import com.client.eurekaclient.services.Web3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/web3Service/")
public class Web3Controller {
    @Autowired
    private Web3Service web3Service;

//    @PostMapping("/sendGame")
//    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
//    public ResponseEntity<Object> sendGame(@RequestBody TransactionRequest transactionRequest) {
//        return web3Service.sendGameTransaction(transactionRequest.recipientAddress, transactionRequest.chainName, transactionRequest.amount, transactionRequest.username);
//    }

}
