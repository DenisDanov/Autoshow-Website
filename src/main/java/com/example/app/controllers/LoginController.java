package com.example.app.controllers;

import com.example.app.data.entities.*;
import com.example.app.services.LoginControllerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.io.IOException;

@Controller
public class LoginController {

    private final LoginControllerService loginControllerService;

    public LoginController(LoginControllerService loginControllerService) {
        this.loginControllerService = loginControllerService;
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("loginUser") User loginUser,
                               HttpServletResponse response,
                               HttpServletRequest request) throws IOException {
        return this.loginControllerService.processLogin(loginUser,response,request);
    }

}

