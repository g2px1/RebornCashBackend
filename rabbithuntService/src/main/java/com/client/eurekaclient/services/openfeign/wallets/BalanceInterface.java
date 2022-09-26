package com.client.eurekaclient.services.openfeign.wallets;

import org.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@FeignClient(name = "balanceService")
public interface BalanceInterface {
    @PostMapping("/unitBalanceService/getBalance/{address}")
    Optional<JSONObject> getBalance(@PathVariable String address);
}
