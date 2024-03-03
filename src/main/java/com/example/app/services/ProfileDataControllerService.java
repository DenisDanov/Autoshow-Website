package com.example.app.services;

import com.example.app.data.DTOs.ProfileResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface ProfileDataControllerService {

    ResponseEntity<ProfileResponse> getProfileData(@RequestParam String id, String authToken,
                                                   HttpServletResponse response);

}
