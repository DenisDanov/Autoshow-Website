package com.example.app.controllers;

import com.example.app.services.NewsletterEmailsControllerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(path = "api/v1/user/newsletter-emails")
public class NewsletterEmailsController {

    private final NewsletterEmailsControllerService newsletterEmailsControllerService;

    public NewsletterEmailsController(NewsletterEmailsControllerService newsletterEmailsControllerService) {
        this.newsletterEmailsControllerService = newsletterEmailsControllerService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addEmail(@RequestParam String email) throws IOException {
        return this.newsletterEmailsControllerService.addEmail(email);
    }

    @DeleteMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribe(@RequestParam String email) {
       return this.newsletterEmailsControllerService.unsubscribe(email);
    }

}
