package com.client.eurekaclient.services.openfeign.verify;

import com.client.eurekaclient.models.DTO.verify.VerifyDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "verifyService")
public interface VerifyInterface {
    @PostMapping("/verifyService/setVerify")
    void setVerify(VerifyDTO verifyDTO);
    @PostMapping("/verifyService/deleteVerify")
    void deleteVerify(VerifyDTO verifyDTO);
    @GetMapping("/verifyService/isExistVerify/{name}")
    void isExistVerify(@PathVariable String name);
}
