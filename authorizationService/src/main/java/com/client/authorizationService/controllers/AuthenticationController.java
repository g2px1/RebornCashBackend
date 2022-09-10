package com.client.authorizationService.controllers;

import com.client.authorizationService.models.DTO.authorization.AuthorizationDTO;
import com.client.authorizationService.models.DTO.authorization.JWTResponseDTO;
import com.client.authorizationService.services.authorization.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authorizationService")
public class AuthenticationController {
    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("/user/authorizeUser")
    public ResponseEntity<Object> authorizeUser(@RequestBody AuthorizationDTO authorizationDTO) {
        return authorizationService.authorizeUser(authorizationDTO);
    }

    @PostMapping("/user/registerUser")
    public ResponseEntity<Object> registerUser(@RequestBody AuthorizationDTO authorizationDTO) {
        return  authorizationService.registerUser(authorizationDTO);
    }

    @PostMapping("/user/resetPassword")
    public ResponseEntity<Object> resetPassword(@RequestBody AuthorizationDTO authorizationDTO) {
        return authorizationService.resetPassword(authorizationDTO);
    }

    @PostMapping("/user/validateToken")
    public ResponseEntity<Object> validateToken(@RequestBody String authorizationHeader) {
        System.out.println(authorizationService.getECKeyData());
        return authorizationService.validateJWT(authorizationHeader);
    }
}
