package com.example.app.controllers;

import com.example.app.data.DTOs.EmailRequestDTO;
import com.example.app.data.DTOs.PasswordRequestDTO;
import com.example.app.data.DTOs.UsernameChangeRequestDTO;
import com.example.app.data.DTOs.UsernameResponseDTO;
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
    public ResponseEntity<UsernameResponseDTO> changeUsername(@RequestBody UsernameChangeRequestDTO request,
                                                              HttpServletResponse response) {
        return changeUserDetailService.changeUsername(request, response);
    }

    @PostMapping("/changeEmail")
    public ResponseEntity<UsernameResponseDTO> changeEmail(@RequestBody EmailRequestDTO request,
                                                           HttpServletResponse response) {
        return changeUserDetailService.changeEmail(request, response);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<UsernameResponseDTO> changePassword(@RequestBody PasswordRequestDTO request,
                                                              HttpServletResponse response) {
       return changeUserDetailService.changePassword(request,response);
    }
}
