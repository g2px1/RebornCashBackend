package com.client.eurekaclient.services.openfeign.wallets;

import com.client.eurekaclient.models.DTO.transactions.TransactionResult;
import com.client.eurekaclient.models.request.web3.TransactionRequest;
import org.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name = "balanceService")
public interface BalanceInterface {
//    @GetMapping("/unitBalanceService/getBalance/{address}")
//    Optional<JSONObject> getBalance(@PathVariable String address);
    @PostMapping("/web3BalanceService/sendNativeTokens")
    TransactionResult sendNativeTokenTransaction(@RequestBody TransactionRequest transactionRequest);
    @PostMapping("/web3BalanceService/isOwnerOfNFT/{username}/{nftIndex}/{chainName}")
    boolean isOwnerOfNFT(@PathVariable String username, @PathVariable long nftIndex, @PathVariable String chainName);
}
