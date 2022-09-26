package com.client.eurekaclient.services.openfeign.wallets;

import com.client.eurekaclient.models.request.web3.ConnectedWallet;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "balanceService")
public interface ConnectedWalletInterface {
    @GetMapping("/connectedWalletsService/findByUsername/{username}")
    Optional<ConnectedWallet> findByUsername(@PathVariable String username);
    @GetMapping("/connectedWalletsService/findByAddress/{address}")
    Optional<ConnectedWallet> findByAddress(@PathVariable String address);
}
