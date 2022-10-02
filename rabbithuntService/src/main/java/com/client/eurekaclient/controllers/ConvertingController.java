package com.client.eurekaclient.controllers;

import com.client.eurekaclient.models.request.rabbithunt.token.ConvertingTokenRequest;
import com.client.eurekaclient.models.request.rabbithunt.token.InvestmentInBurgerRequest;
import com.client.eurekaclient.services.rabbithunt.converter.TokensConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/rabbitHuntService/converting/")
public class ConvertingController {
    @Autowired
    private TokensConverter tokensConverter;

    @PostMapping("/convertLayer1")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> convertLayer1TokensIntoGame(@RequestBody ConvertingTokenRequest convertingTokenRequest, Authentication authentication) {
        return tokensConverter.convertLayer1TokensIntoGame(convertingTokenRequest, authentication.getName());
    }

    @PostMapping("/investInBurger")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> investInBurger(@RequestBody InvestmentInBurgerRequest investmentInBurgerRequest, Authentication authentication) {
        return tokensConverter.investInBurger(investmentInBurgerRequest, authentication.getName());
    }
}
