package com.example.app.services;

import com.example.app.data.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.IOException;

public interface LoginControllerService {

    String processLogin(@ModelAttribute("loginUser") User loginUser,
                        HttpServletResponse response,
                        HttpServletRequest request) throws IOException;

}
