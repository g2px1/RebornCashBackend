package com.client.eurekaclient.controllers;

import com.client.eurekaclient.models.request.rabbithunt.token.CreateTokenRequest;
import com.client.eurekaclient.models.request.rabbithunt.token.TokenSeekingRequest;
import com.client.eurekaclient.models.request.unit.TransferTokensRequests;
import com.client.eurekaclient.services.rabbithunt.transaction.SecuredTxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/rabbitHuntService/securedTxService/")
public class SecuredTxController {
    @Autowired
    private SecuredTxService securedTxService;

    @PostMapping("/createToken")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> createToken(@RequestBody CreateTokenRequest createTokenRequest) {
        return securedTxService.createToken(createTokenRequest);
    }

    @PostMapping("/loadTokens")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> createTrap(@RequestBody TokenSeekingRequest tokenSeekingRequest) {
        return securedTxService.loadTokens(tokenSeekingRequest);
    }

    @GetMapping("/balanceOf/{address}")
    public ResponseEntity<Object> balanceOf(@PathVariable String address) {
        return securedTxService.balanceOf(address);
    }

    @PostMapping("/distributeToken/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> distributeToken(@RequestBody TransferTokensRequests transferTokensRequests) {
        return securedTxService.distributeToken(transferTokensRequests);
    }
}
