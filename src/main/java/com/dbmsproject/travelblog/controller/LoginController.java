package com.dbmsproject.travelblog.controller;

import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

///Controller for login
@Controller
public class LoginController {

    private Logger logger = Logger.getLogger(getClass().getName());

    ///Login form
    @GetMapping("/login")
    public String loginPage() {
        logger.info("LoginController: Show login form");

        return "login";
    }
}
