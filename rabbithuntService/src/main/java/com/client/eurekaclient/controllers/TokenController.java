package com.client.eurekaclient.controllers;

import com.client.eurekaclient.models.request.merchant.BuyTokensFromMerchantRequest;
import com.client.eurekaclient.services.rabbithunt.market.tokens.TokensService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/oreHuntService/coins/")
public class TokenController {
    @Autowired
    private TokensService tokensService;

    @PostMapping("/buyCoinsFromMerchant")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> sellTokens(@RequestBody BuyTokensFromMerchantRequest buyTokensFromMerchantRequest, Authentication authentication) {
        return tokensService.buySilverCoins(buyTokensFromMerchantRequest, authentication.getName());
    }
}
