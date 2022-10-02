package com.client.eurekaclient.controllers;

import com.client.eurekaclient.models.request.rabbithunt.token.InvestmentInBurgerRequest;
import com.client.eurekaclient.models.request.rabbithunt.trap.BuyCellsRequest;
import com.client.eurekaclient.services.rabbithunt.trap.UserTrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/rabbitHuntService/traps/")
public class TrapController {
    @Autowired
    private UserTrapService userTrapService;

    @PostMapping("/loadTraps")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> loadTraps(@RequestBody HashMap<String, Integer> page) {
        return userTrapService.loadTraps(page);
    }

    @PostMapping("/buyTrapCells")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> buyTrapCells(@RequestBody BuyCellsRequest buyCellsRequest, Authentication authentication) {
        return userTrapService.buyCells(buyCellsRequest, authentication.getName());
    }

    @PostMapping("/investInBurger")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> investInBurger(@RequestBody InvestmentInBurgerRequest investmentInBurgerRequest, Authentication authentication) {
        return userTrapService.investInBurger(investmentInBurgerRequest, authentication.getName());
    }
}
