package com.example.app.services.impl;

import com.example.app.services.EmailsControllerService;
import com.example.app.services.SendGridEmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailsControllerServiceImpl implements EmailsControllerService {

    private final SendGridEmailService sendGridEmailService;

    public EmailsControllerServiceImpl(SendGridEmailService sendGridEmailService) {
        this.sendGridEmailService = sendGridEmailService;
    }

    @Override
    public ResponseEntity<String> sendEmail(String to, String subject, String text) {
        try {
            sendGridEmailService.sendSimpleMessage(to, subject, text);
            return ResponseEntity.ok("Email sent successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");
        }
    }
}
