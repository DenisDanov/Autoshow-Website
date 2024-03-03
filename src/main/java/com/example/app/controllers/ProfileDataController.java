package com.example.app.controllers;

import com.example.app.data.DTOs.ProfileResponse;
import com.example.app.services.ProfileDataControllerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileDataController {

    private final ProfileDataControllerService profileDataControllerService;

    public ProfileDataController(ProfileDataControllerService profileDataControllerService) {
        this.profileDataControllerService = profileDataControllerService;
    }

    @GetMapping("/get")
    public ResponseEntity<ProfileResponse> getProfileData(@RequestParam String id, String authToken,
                                                          HttpServletResponse response) {
        return this.profileDataControllerService.getProfileData(id, authToken, response);
    }
}
