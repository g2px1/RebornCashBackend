package com.client.verifyService.controllers;

import com.client.verifyService.services.VerifyService;
import com.client.verifyService.utilities.DTO.verify.VerifyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/verifyService")
public class VerifyController {
    @Autowired
    private VerifyService verifyService;

    @PostMapping("/setVerify")
    public void setVerify(VerifyDTO verifyDTO) { verifyService.setVerify(verifyDTO); }
    @PostMapping("/deleteVerify")
    public void deleteVerify(VerifyDTO verifyDTO) { verifyService.deleteVerify(verifyDTO); }
    @GetMapping("/isExistVerify/{name}")
    public void isExistVerify(@PathVariable String name) { verifyService.existsVerify(name); }
}