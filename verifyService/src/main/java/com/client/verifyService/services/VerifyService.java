package com.client.verifyService.services;

import com.client.verifyService.models.Verify2FA;
import com.client.verifyService.repositories.VerifyRepository;
import com.client.verifyService.utilities.DTO.verify.VerifyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerifyService {
    @Autowired
    private VerifyRepository verifyRepository;

    public void setVerify(VerifyDTO verifyDTO) { verifyRepository.save(new Verify2FA(verifyDTO.username, verifyDTO.authorized)); }
    public void deleteVerify(VerifyDTO verifyDTO) { verifyRepository.deleteAllByUsername(verifyDTO.username); }
    public boolean existsVerify(String username) { return verifyRepository.existsByUsername(username); }
}
