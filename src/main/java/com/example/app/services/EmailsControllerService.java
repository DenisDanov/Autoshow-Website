package com.example.app.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface EmailsControllerService {

    ResponseEntity<String> sendEmail(@RequestParam String to,
                                     @RequestParam String subject,
                                     @RequestParam String text);

}
