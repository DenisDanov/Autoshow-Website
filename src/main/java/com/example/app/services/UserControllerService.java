package com.example.app.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

public interface UserControllerService {

    ResponseEntity<String> initiatePasswordReset(@RequestParam String email) throws IOException;

}
