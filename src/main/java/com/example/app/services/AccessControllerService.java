package com.example.app.services;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.net.URISyntaxException;

public interface AccessControllerService {

    ModelAndView checkAccess(String pageUrl, String car, HttpServletRequest request);

    ModelAndView checkAccess(String pageUrl, HttpServletRequest request) throws URISyntaxException;
}
