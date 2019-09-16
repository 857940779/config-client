package com.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
    private static final String VIEW_LOGIN = "login";

    @RequestMapping("login")
    public String login() {
        return VIEW_LOGIN;
    }
}
