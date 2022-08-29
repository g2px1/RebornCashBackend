package com.client.eurekaclient.testControllers;

import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {
    @RequestMapping("/users/{id}/statistic")
    public String getStatistic(@PathVariable String id) {
        return id.equals("john") ? "John" : "Emma";
    }
}
