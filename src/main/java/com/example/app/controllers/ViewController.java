package com.example.app.controllers;

import com.example.app.data.entities.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("profile")
    public String profile() {
        return "profile";
    }

    @GetMapping("index")
    public String home() {
        return "index";
    }

    @GetMapping("auto-show")
    public String autoshow() {
        return "auto-show";
    }

    @GetMapping("newsletter-unsubscribe.html")
    public String newsletterUnsubscribe() {
        return "newsletter-unsubscribe";
    }

    @GetMapping("reset-password.html")
    public String resetPassword() {
        return "reset-password";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginUser", new User());
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
}
