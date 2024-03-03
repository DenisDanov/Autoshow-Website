package com.example.app.services.impl;

import com.example.app.data.entities.NewsletterEmails;
import com.example.app.services.NewsletterEmailsControllerService;
import com.example.app.services.NewsletterEmailsService;
import com.example.app.services.SendGridEmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class NewsletterEmailsControllerServiceImpl implements NewsletterEmailsControllerService {

    private final NewsletterEmailsService newsletterEmailsService;

    private final SendGridEmailService sendGridEmailService;

    public NewsletterEmailsControllerServiceImpl(NewsletterEmailsService newsletterEmailsService,
                                                 SendGridEmailService sendGridEmailService) {
        this.newsletterEmailsService = newsletterEmailsService;
        this.sendGridEmailService = sendGridEmailService;
    }

    @Override
    public ResponseEntity<String> addEmail(String email) throws IOException {
        NewsletterEmails newsletterEmail = newsletterEmailsService.findByEmail(String.valueOf(email));
        String emailRegex = "[A-Za-z0-9]+[@][A-Za-z]{2,}[.][a-zA-Z]{2,}";
        if (newsletterEmail == null) {
            if (String.valueOf(email).matches(emailRegex)) {
                newsletterEmail = new NewsletterEmails(String.valueOf(email));
                newsletterEmailsService.save(newsletterEmail);
                sendGridEmailService.sendSimpleMessage(String.valueOf(email),"Thanks for subscribing to our Newsletter!","You will be receiving emails from us every two weeks with news about the site.");
                return ResponseEntity.ok("Thank you for subscribing to our Newsletter!");
            } else {
                return ResponseEntity.ok("Please enter a valid email and try again.");
            }
        } else {
            return ResponseEntity.ok("This email is already subscribed to the newsletter.");
        }
    }

    @Override
    public ResponseEntity<String> unsubscribe(String email) {
        NewsletterEmails newsletterEmail = newsletterEmailsService.findByEmail(String.valueOf(email));
        if (newsletterEmail != null) {
            newsletterEmailsService.delete(newsletterEmail);
            return ResponseEntity.ok("You have been unsubscribed successfully.");
        } else {
            return ResponseEntity.ok("The provided email is not subscribed to our Newsletter.");
        }
    }
}
