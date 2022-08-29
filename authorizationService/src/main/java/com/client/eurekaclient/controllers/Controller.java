package com.client.eurekaclient.controllers;

import com.client.eurekaclient.TestUserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {
    @Autowired
    public TestUserInterface testUserInterface;

    @GetMapping("/get")
    public String getTest() {
        System.out.println(testUserInterface.getStatistic("john"));
        return "Hello world!";
    }
}
