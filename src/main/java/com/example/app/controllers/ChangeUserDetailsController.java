package com.example.app.controllers;

import com.example.app.data.DTOs.EmailRequest;
import com.example.app.data.DTOs.PasswordRequest;
import com.example.app.data.DTOs.UsernameChangeRequest;
import com.example.app.data.DTOs.UsernameResponse;
import com.example.app.services.ChangeUserDetailService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ChangeUserDetailsController {

    private final ChangeUserDetailService changeUserDetailService;

    public ChangeUserDetailsController(ChangeUserDetailService changeUserDetailService) {
        this.changeUserDetailService = changeUserDetailService;
    }

    @PostMapping("/changeUsername")
    public ResponseEntity<UsernameResponse> changeUsername(@RequestBody UsernameChangeRequest request,
                                                           HttpServletResponse response) {
        return changeUserDetailService.changeUsername(request, response);
    }

    @PostMapping("/changeEmail")
    public ResponseEntity<UsernameResponse> changeEmail(@RequestBody EmailRequest request,
                                                        HttpServletResponse response) {
        return changeUserDetailService.changeEmail(request, response);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<UsernameResponse> changePassword(@RequestBody PasswordRequest request,
                                                           HttpServletResponse response) {
       return changeUserDetailService.changePassword(request,response);
    }
}
