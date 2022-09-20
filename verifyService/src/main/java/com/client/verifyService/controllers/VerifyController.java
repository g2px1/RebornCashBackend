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
    public void setVerify(@RequestBody VerifyDTO verifyDTO) { verifyService.setVerify(verifyDTO); }
    @PostMapping("/deleteVerify")
    public void deleteVerify(@RequestBody VerifyDTO verifyDTO) { verifyService.deleteVerify(verifyDTO); }
    @GetMapping("/isExistVerify/{name}")
    public boolean isExistVerify(@PathVariable String name) { return verifyService.existsVerify(name); }
}
