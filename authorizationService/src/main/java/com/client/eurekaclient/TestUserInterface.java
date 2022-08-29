package com.client.eurekaclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;

@FeignClient(name = "userService")
public interface TestUserInterface {
    @GetMapping("/users/{id}/statistic")
    String getStatistic(@PathVariable String id);
}
