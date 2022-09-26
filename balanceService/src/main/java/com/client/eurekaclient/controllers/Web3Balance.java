package com.client.eurekaclient.controllers;

import com.client.eurekaclient.models.request.web3.TransactionRequest;
import com.client.eurekaclient.services.web3.TransactionService;
import com.client.eurekaclient.services.web3.Web3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/web3BalanceService/")
public class Web3Balance {
    @Autowired
    private Web3Service web3Service;
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/depositGame")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> depositGame(@RequestBody TransactionRequest transactionRequest, Authentication authentication) {
        return transactionService.saveTransaction(authentication.getName(), transactionRequest.chainName, transactionRequest.hash);
    }

    @PostMapping("/withdrawGame")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> withdrawGame(@RequestBody TransactionRequest transactionRequest, Authentication authentication) {
        return web3Service.sendGameTransaction(transactionRequest.recipientAddress, transactionRequest.chainName, transactionRequest.amount, authentication.getName(), transactionRequest.code);
    }

    @PostMapping("/isOwnerOfNFT/{username}/{nftIndex}/{chainName}")
    public boolean isOwnerOfNFT(@PathVariable String username, @PathVariable long nftIndex, @PathVariable String chainName) {
        return web3Service.isWalletOwnerOfNftByIndex(username, nftIndex, chainName);
    }
}
