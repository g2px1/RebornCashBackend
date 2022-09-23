package com.client.eurekaclient.controllers;

import com.client.eurekaclient.services.rabbithunt.converter.TokensConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/rabbitHuntService")
public class ConvertingController {
    @Autowired
    private TokensConverter tokensConverter;

}
