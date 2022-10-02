package com.client.eurekaclient.controllers;

import com.client.eurekaclient.models.request.rabbithunt.trap.CloseTrapRequest;
import com.client.eurekaclient.models.request.rabbithunt.trap.TrapRequest;
import com.client.eurekaclient.services.rabbithunt.trap.SecuredTrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/rabbitHuntService/securedTrapService/")
public class SecuredTrapController {
    @Autowired
    private SecuredTrapService securedTrapService;

    @PostMapping("/createTrap")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> createTrap(@RequestBody TrapRequest trapRequest) {
        return securedTrapService.createTrap(trapRequest);
    }

    @PostMapping("/closeTrap")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> closeTrap(@RequestBody CloseTrapRequest closeTrapRequest) {
        return securedTrapService.closeTrap(closeTrapRequest);
    }

    @PostMapping("/editTrap")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> closeTrap(@RequestBody TrapRequest trapRequest) {
        return securedTrapService.editTrap(trapRequest);
    }

    @PostMapping("/loadTrap")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> loadTrap(@RequestBody HashMap<String, String> trap) {
        return securedTrapService.loadTrap(trap);
    }

    @PostMapping("/loadTraps")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> loadTraps(@RequestBody HashMap<String, Integer> pageNumber) {
        return securedTrapService.loadTraps(pageNumber);
    }

    @GetMapping("/getTrapsImage/{trapImage}")
    public ResponseEntity<Object> getTrapsImage(@PathVariable String trapImage) {
        return securedTrapService.getTrapsImage(trapImage);
    }

    @PostMapping("/getTrapsImages/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getTrapsImages() {
        return securedTrapService.loadTrapsImages();
    }
}
