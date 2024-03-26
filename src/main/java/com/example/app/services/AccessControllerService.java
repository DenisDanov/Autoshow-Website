package com.example.app.services;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import java.net.URISyntaxException;

public interface AccessControllerService {

    ModelAndView checkAccess(String pageUrl, String car, HttpServletRequest request, HttpServletResponse response);

    ModelAndView checkAccess(String pageUrl, HttpServletRequest request, HttpServletResponse response) throws URISyntaxException;
}
