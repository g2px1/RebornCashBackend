package com.client.eurekaclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;

@FeignClient(name = "userService")
public interface TestUserInterface {
    @GetMapping("/users/{id}/statistic")
    HashMap<String, Object> getStatistic(@PathVariable String id);
}
