package com.client.eurekaclient.controllers;

import com.client.eurekaclient.services.web3.Web3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/oreChainAddress/")
public class AddressController {
    @Autowired
    private Web3Service web3Service;

    @GetMapping("/getBlockchainWalletAddress/{chainName}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> getBlockchainWalletAddress(@PathVariable String chainName) {
        return web3Service.getAddress(chainName);
    }
}
