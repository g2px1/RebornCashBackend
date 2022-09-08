package com.client.eurekaclient.controllers;

import com.client.eurekaclient.models.DTO.unit.TransferTokensRequests;
import com.client.eurekaclient.services.unit.UnitService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/unitService")
public class UnitController {
    @Autowired
    private UnitService unitService;

    @PostMapping("/sendTokens")
    public JSONObject authorizeUser(@RequestBody TransferTokensRequests transferTokensRequests) {
        return unitService.sendTokens(transferTokensRequests);
    }
}
