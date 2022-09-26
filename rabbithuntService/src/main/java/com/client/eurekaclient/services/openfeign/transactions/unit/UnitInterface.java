package com.client.eurekaclient.services.openfeign.transactions.unit;

import com.client.eurekaclient.models.request.unit.TransferTokensRequests;
import org.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "balanceService")
public interface UnitInterface {
    @PostMapping("/unitBalanceService/sendTokens")
    JSONObject sendTokens(@RequestBody TransferTokensRequests transferTokensRequests);
}
