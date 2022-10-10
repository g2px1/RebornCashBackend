package com.client.eurekaclient.controllers;

import com.client.eurekaclient.models.request.web3.ConnectWalletRequest;
import com.client.eurekaclient.models.web3.ConnectedWallet;
import com.client.eurekaclient.repositories.ConnectedWalletsRepository;
import com.client.eurekaclient.services.web3.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/connectedWalletsService/")
public class ConnectedWallets {
    @Autowired
    private ConnectedWalletsRepository connectedWalletsRepository;
    @Autowired
    private WalletService walletService;

    @GetMapping("/findByUsername/{username}")
    public Optional<ConnectedWallet> findByUsername(@PathVariable String username) {
        return connectedWalletsRepository.findByUsername(username);
    }
    @GetMapping("/findByAddress/{address}")
    public Optional<ConnectedWallet> findByAddress(@PathVariable String address) {
        return connectedWalletsRepository.findByAddress(address);
    }
    @PostMapping("/connectWallet")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> connectedWallet(@RequestBody ConnectWalletRequest connectWalletRequest, Authentication authentication) {
        return walletService.connectWallet(connectWalletRequest.message, connectWalletRequest.requestedAddress, connectWalletRequest.chainName, authentication.getName());
    }
}
