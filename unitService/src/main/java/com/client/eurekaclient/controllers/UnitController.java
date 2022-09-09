package com.client.eurekaclient.controllers;

import com.client.eurekaclient.models.DTO.unit.TransferTokensRequests;
import com.client.eurekaclient.services.unit.UnitService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/unitService")
public class UnitController {
    @Autowired
    private UnitService unitService;

    @PostMapping("/sendTokens")
    public JSONObject sendTokens(@RequestBody TransferTokensRequests transferTokensRequests) {
        return unitService.sendTokens(transferTokensRequests);
    }
    @PostMapping("/sendUnits")
    public JSONObject sendUnits(@RequestBody TransferTokensRequests transferTokensRequests) {
        return unitService.sendUnits(transferTokensRequests);
    }
    @PostMapping("/createToken")
    public JSONObject sendUnits(@RequestBody String bytecode) {
        return unitService.createToken(bytecode);
    }
    @PostMapping("/findTx")
    public JSONObject findTx(@RequestBody String hash) {
        return unitService.findTx(hash);
    }
    @GetMapping("/blockHeight")
    public JSONObject blockHeight() {
        return unitService.getBlockHeight();
    }
    @GetMapping("/txPool")
    public JSONObject txPool() {
        return unitService.getTxPool();
    }
    @PostMapping("/getBalance")
    public JSONObject txPool(@RequestBody String address) {
        return unitService.getBalance(address);
    }
}
