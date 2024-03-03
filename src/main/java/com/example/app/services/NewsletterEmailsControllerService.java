package com.example.app.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

public interface NewsletterEmailsControllerService {

    ResponseEntity<String> addEmail(@RequestParam String email) throws IOException;

    ResponseEntity<String> unsubscribe(@RequestParam String email);
}
