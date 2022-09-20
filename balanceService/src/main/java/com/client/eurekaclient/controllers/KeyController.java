package com.client.eurekaclient.controllers;

import com.client.eurekaclient.models.DTO.JWT.JWS;
import com.client.eurekaclient.utilities.JWT.AuthenticationEntryPointJWT;
import com.nimbusds.jose.jwk.ECKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/keyBalanceService/")
public class KeyController {
    @Autowired
    private JWS jws;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationEntryPointJWT.class);

    @PostMapping("/setECKey")
    public void setECKey(@RequestBody Map<String, Object> ecKey) {
        try {
            jws.setEcKey(ECKey.parse(ecKey));
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
    }
}
