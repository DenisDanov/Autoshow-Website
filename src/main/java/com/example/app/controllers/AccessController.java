package com.example.app.controllers;

import com.example.app.services.AccessControllerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AccessController {

    private final AccessControllerService accessControllerService;

    public AccessController(AccessControllerService accessControllerService) {
        this.accessControllerService = accessControllerService;
    }

    @GetMapping("showroom.html")
    public ModelAndView showroomPage(@RequestParam String car, HttpServletRequest request) {
        return accessControllerService.checkAccess("showroom", car, request);
    }

    @GetMapping("cars-info.html")
    public ModelAndView carInfoPage(@RequestParam String car, HttpServletRequest request) {
        return accessControllerService.checkAccess("cars-info", car, request);
    }

}
