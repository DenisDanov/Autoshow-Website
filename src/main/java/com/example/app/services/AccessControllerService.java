package com.example.app.services;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

public interface AccessControllerService {

    ModelAndView checkAccess(String pageUrl, String car, HttpServletRequest request);

}
