package com.client.userService.services.openfeign.key;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(name = "authorizationService")
public interface KeyInterface {
    @GetMapping("/keyService/getKey")
    Map<String, Object> getKey();
}
