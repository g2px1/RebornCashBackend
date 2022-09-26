package com.client.eurekaclient.controllers;

import com.client.eurekaclient.models.request.unit.TransferTokensRequests;
import com.client.eurekaclient.services.unit.UnitService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/unitBalanceService/")
public class UnitBalance {
    @Autowired
    private UnitService unitService;

    @PostMapping("/sendTokens")
    public JSONObject sendTokens(@RequestBody TransferTokensRequests transferTokensRequests) {return unitService.sendTokens(transferTokensRequests);}
    @PostMapping("/sendUnits")
    public JSONObject sendUnits(@RequestBody TransferTokensRequests transferTokensRequests) {return unitService.sendUnits(transferTokensRequests);}
    @PostMapping("/createToken")
    public JSONObject createToken(@RequestBody String bytecode) {
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
    @PostMapping("/getBalance/{address}")
    public Optional<JSONObject> getBalance(@PathVariable String address) {
        return unitService.getBalance(address);
    }
}
