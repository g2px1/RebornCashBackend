package com.client.eurekaclient.services.openfeign.verify;

import com.client.eurekaclient.models.DTO.verify.VerifyDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "verifyService")
public interface VerifyInterface {
    @PostMapping("/verifyService/setVerify")
    void setVerify(@RequestBody VerifyDTO verifyDTO);
    @PostMapping("/verifyService/deleteVerify")
    void deleteVerify(@RequestBody VerifyDTO verifyDTO);
    @GetMapping("/verifyService/isExistVerify/{name}")
    boolean isExistVerify(@PathVariable String name);
}
