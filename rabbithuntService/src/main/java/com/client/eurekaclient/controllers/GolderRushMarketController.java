package com.client.eurekaclient.controllers;

import com.client.eurekaclient.models.request.cells.BuyCellsPacks;
import com.client.eurekaclient.models.request.cells.SellCellsRequest;
import com.client.eurekaclient.models.request.cells.WithdrawCellsPacks;
import com.client.eurekaclient.models.request.goldenrush.token.market.BuyTokensRequest;
import com.client.eurekaclient.models.request.goldenrush.token.market.SellTokensRequest;
import com.client.eurekaclient.services.rabbithunt.market.cells.CellsService;
import com.client.eurekaclient.services.rabbithunt.market.tokens.TokensService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/oreHuntService/market/")
public class GolderRushMarketController {
    @Autowired
    private TokensService tokensService;
    @Autowired
    private CellsService cellsService;

    @PostMapping("/sellTokens")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> sellTokens(@RequestBody SellTokensRequest sellTokensRequest, Authentication authentication) {
        return tokensService.sellToken(sellTokensRequest, authentication.getName());
    }

    @PostMapping("/buyTokens")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> buyTokens(@RequestBody BuyTokensRequest buyTokensRequest, Authentication authentication) {
        return tokensService.buyToken(buyTokensRequest, authentication.getName());
    }

    @PostMapping("/sellCellsPack")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> sellCellsPack(@RequestBody SellCellsRequest sellCellsRequest, Authentication authentication) {
        return cellsService.sellCells(sellCellsRequest, authentication.getName());
    }

    @PostMapping("/buyCellsPack")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> buyCellsPack(@RequestBody BuyCellsPacks buyCellsPacks, Authentication authentication) {
        return cellsService.buyCellsPack(buyCellsPacks, authentication.getName());
    }

    @PostMapping("/withdrawCellsPacks")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> withdrawCellsPacks(@RequestBody WithdrawCellsPacks withdrawCellsPacks, Authentication authentication) {
        return cellsService.withdrawCellsPacks(withdrawCellsPacks, authentication.getName());
    }
}
