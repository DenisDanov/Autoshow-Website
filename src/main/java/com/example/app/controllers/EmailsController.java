package com.example.app.controllers;

import com.example.app.services.EmailsControllerService;
import com.example.app.services.SendGridEmailService;
import com.example.app.services.impl.SendGridEmailServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class EmailsController {

    private final EmailsControllerService emailsControllerService;

    public EmailsController(EmailsControllerService emailsControllerService) {
        this.emailsControllerService = emailsControllerService;
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestParam String to,
                                            @RequestParam String subject,
                                            @RequestParam String text) {
        return emailsControllerService.sendEmail(to,subject,text);
    }
}
