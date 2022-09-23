package com.client.eurekaclient.controllers;

import com.client.eurekaclient.models.web3.ConnectedWallet;
import com.client.eurekaclient.repositories.ConnectedWalletsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/connectedWalletsService/")
public class ConnectedWallets {
    @Autowired
    private ConnectedWalletsRepository connectedWalletsRepository;

    @GetMapping("/findByUsername/{username}")
    public ConnectedWallet findByUsername(@PathVariable String username) {
        return connectedWalletsRepository.findByUsername(username);
    }

    @GetMapping("/findByAddress/{address}")
    public ConnectedWallet findByAddress(@PathVariable String address) {
        return connectedWalletsRepository.findByAddress(address);
    }
}
