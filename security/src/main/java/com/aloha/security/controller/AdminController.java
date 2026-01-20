package com.aloha.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @GetMapping("")
    public String admin() {
        return "admin/index";
    }
    

}
