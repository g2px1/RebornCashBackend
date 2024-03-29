package com.client.eurekaclient.controllers;

import com.client.eurekaclient.models.response.BlockchainDataResponse;
import com.client.eurekaclient.models.web3.BlockchainData;
import com.client.eurekaclient.repositories.BlockchainsRepository;
import com.client.eurekaclient.services.web3.Web3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/secureWEB3/")
public class Web3Admin {
    @Autowired
    private BlockchainsRepository blockchainsRepository;
    @Autowired
    private Web3Service web3Service;

    @PostMapping("/addBlockchain")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> addBlockchain(@RequestBody BlockchainData blockchainData) {
        return web3Service.addBlockchain(blockchainData);
    }

    @PostMapping("/changeBlockchain")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> changeBlockchain(@RequestBody BlockchainData blockchainData) {
        return web3Service.changeBlockchain(blockchainData);
    }

    @GetMapping("/getBlockchainData/{chainName}")
    public Optional<BlockchainDataResponse> getBlockchainData(@PathVariable String chainName) {
        return web3Service.getBlockchain(chainName);
    }
}
