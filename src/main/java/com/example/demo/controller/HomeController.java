package com.example.demo.controller;

import com.example.demo.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {

    @Autowired
    UserService userService;
    @GetMapping("/")
    public String home(){
        log.info("GET /");
        System.out.println("UserService : " + userService);
        return "index";
    }

    @GetMapping("/login")
    public void login(){
        log.info("GET /login...");
    }

    @GetMapping("templates")
    public String templates(){
        return "templates";
    }

}
