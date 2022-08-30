package com.client.authorizationService.controllers;

import com.client.authorizationService.services.authorization.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authorizationService")
public class AuthorizationController {
    @Autowired
    private AuthorizationService authorizationService;
}
