package com.example.app.controllers;

import com.example.app.services.AccessControllerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.net.URISyntaxException;

@Controller
public class AccessController {

    private final AccessControllerService accessControllerService;

    public AccessController(AccessControllerService accessControllerService) {
        this.accessControllerService = accessControllerService;
    }

    @GetMapping("showroom.html")
    public ModelAndView showroomPage(@RequestParam String car, HttpServletRequest request, HttpServletResponse response) {
        return accessControllerService.checkAccess("showroom", car, request, response);
    }

    @GetMapping("profile")
    public ModelAndView showroomPage(HttpServletRequest request, HttpServletResponse response) throws URISyntaxException {
        return accessControllerService.checkAccess("profile", request, response);
    }

    @GetMapping("cars-info.html")
    public ModelAndView carInfoPage(@RequestParam String car, HttpServletRequest request, HttpServletResponse response) {
        return accessControllerService.checkAccess("cars-info", car, request, response);
    }

}
