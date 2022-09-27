package com.client.eurekaclient.services.openfeign.transactions.unit;

import com.client.eurekaclient.models.request.unit.TransferTokensRequests;
import org.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name = "balanceService")
public interface UnitInterface {
    @PostMapping("/unitBalanceService/sendTokens")
    JSONObject sendTokens(@RequestBody TransferTokensRequests transferTokensRequests);
    @PostMapping("/createToken/{bytecode}")
    JSONObject createToken(@PathVariable String bytecode);
    @PostMapping("/getBalance/{address}")
    Optional<JSONObject> getBalance(@PathVariable String address);
}
