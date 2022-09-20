package com.client.authorizationService.controllers;

import com.client.authorizationService.services.authorization.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/keyService")
public class KeyController {
    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping("/getKey")
    public Map<String, Object> getKey() {
        System.out.println(authorizationService.getECKeyData());
        return authorizationService.getECKeyData();
    }
}
