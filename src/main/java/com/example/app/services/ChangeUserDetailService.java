package com.example.app.services;

import com.example.app.data.DTOs.EmailRequestDTO;
import com.example.app.data.DTOs.PasswordRequestDTO;
import com.example.app.data.DTOs.UsernameChangeRequestDTO;
import com.example.app.data.DTOs.UsernameResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;


public interface ChangeUserDetailService {

    ResponseEntity<UsernameResponseDTO> changeUsername(@RequestBody UsernameChangeRequestDTO request,
                                                       HttpServletResponse response);

    ResponseEntity<UsernameResponseDTO> changeEmail(@RequestBody EmailRequestDTO request,
                                                    HttpServletResponse response);

    ResponseEntity<UsernameResponseDTO> changePassword(PasswordRequestDTO request, HttpServletResponse response);
}
