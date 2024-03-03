package com.example.app.services;

import com.example.app.data.DTOs.EmailRequest;
import com.example.app.data.DTOs.PasswordRequest;
import com.example.app.data.DTOs.UsernameChangeRequest;
import com.example.app.data.DTOs.UsernameResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;


public interface ChangeUserDetailService {

    ResponseEntity<UsernameResponse> changeUsername(@RequestBody UsernameChangeRequest request,
                                                    HttpServletResponse response);

    ResponseEntity<UsernameResponse> changeEmail(@RequestBody EmailRequest request,
                                                 HttpServletResponse response);

    ResponseEntity<UsernameResponse> changePassword(PasswordRequest request, HttpServletResponse response);
}
