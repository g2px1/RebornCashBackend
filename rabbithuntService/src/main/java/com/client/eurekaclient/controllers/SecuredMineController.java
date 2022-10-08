package com.client.eurekaclient.controllers;

import com.client.eurekaclient.models.request.goldenrush.mine.CloseMineRequest;
import com.client.eurekaclient.models.request.goldenrush.mine.MineRequest;
import com.client.eurekaclient.services.rabbithunt.mine.SecuredMineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/oreChainService/securedMineService/")
public class SecuredMineController {
    @Autowired
    private SecuredMineService securedMineService;

    @PostMapping("/createMine")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> createTrap(@ModelAttribute MineRequest mineRequest) {
        return securedMineService.createMine(mineRequest);
    }

    @PostMapping("/closeMine")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> closeTrap(@RequestBody CloseMineRequest closeMineRequest) {
        return securedMineService.closeMine(closeMineRequest);
    }

    @PostMapping("/editMine")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> closeTrap(@ModelAttribute MineRequest mineRequest) {
        return securedMineService.editMine(mineRequest);
    }

    @PostMapping("/loadMine")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> loadTrap(@RequestBody HashMap<String, String> mine) {
        return securedMineService.loadMine(mine);
    }

    @PostMapping("/loadMines")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> loadMines(@RequestBody HashMap<String, Integer> pageNumber) {
        return securedMineService.loadMines(pageNumber);
    }

    @GetMapping("/getMinesImage/{mineImage}")
    public ResponseEntity<Object> getMinesImage(@PathVariable String mineImage) {
        return securedMineService.getMinesImage(mineImage);
    }

    @PostMapping("/getMinesImages/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getMinesImages() {
        return securedMineService.loadMinesImages();
    }
}
