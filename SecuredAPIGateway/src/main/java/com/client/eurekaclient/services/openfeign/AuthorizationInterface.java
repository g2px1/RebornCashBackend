package com.client.eurekaclient.services.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "authorizationService")
public interface AuthorizationInterface {
    @PostMapping("/user/validateToken")
    Boolean validateToken(@RequestBody String authorizationHeader);
}
