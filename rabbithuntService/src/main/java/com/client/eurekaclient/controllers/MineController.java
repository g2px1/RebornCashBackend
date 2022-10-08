package com.client.eurekaclient.controllers;

import com.client.eurekaclient.models.request.goldenrush.mine.BuyCellsRequest;
import com.client.eurekaclient.services.rabbithunt.mine.UserMineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/oreChainService/mines/")
public class MineController {
    @Autowired
    private UserMineService userMineService;

    @PostMapping("/loadMines")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> loadMines(@RequestBody HashMap<String, Integer> page) {
        return userMineService.loadMines(page);
    }

    @GetMapping("/getMinesImage/{mineImage}")
    public ResponseEntity<Object> getMinesImage(@PathVariable String mineImage) {
        return userMineService.getMinesImage(mineImage);
    }

    @PostMapping("/buyMineCells")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> buyMineCells(@RequestBody BuyCellsRequest buyCellsRequest, Authentication authentication) {
        return userMineService.buyCells(buyCellsRequest, authentication.getName());
    }
}
