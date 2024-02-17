package com.example.demo.emailApp.newsLetterEmails;

import com.example.demo.emailApp.SendGridEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(path = "api/v1/user/newsletter-emails")
public class NewsletterEmailsController {
    private final NewsletterEmailsRepository newsletterEmailsRepository;
    private final SendGridEmailService sendGridEmailService;

    @Autowired
    public NewsletterEmailsController(NewsletterEmailsRepository newsletterEmailsRepository,
                                      SendGridEmailService sendGridEmailService) {
        this.newsletterEmailsRepository = newsletterEmailsRepository;
        this.sendGridEmailService = sendGridEmailService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addEmail(@RequestParam String email) throws IOException {
        NewsletterEmails newsletterEmail = newsletterEmailsRepository.findByEmail(String.valueOf(email));
        String emailRegex = "[A-Za-z0-9]+[@][A-Za-z]{2,}[.][a-zA-Z]{2,}";
        if (newsletterEmail == null) {
            if (String.valueOf(email).matches(emailRegex)) {
                newsletterEmail = new NewsletterEmails(String.valueOf(email));
                newsletterEmailsRepository.save(newsletterEmail);
                sendGridEmailService.sendSimpleMessage(String.valueOf(email),"Thanks for subscribing to our Newsletter!","You will be receiving emails from us every two weeks with news about the site.");
                return ResponseEntity.ok("Thank you for subscribing to our Newsletter!");
            } else {
                return ResponseEntity.ok("Please enter a valid email and try again.");
            }
        } else {
            return ResponseEntity.ok("This email is already subscribed to the newsletter.");
        }
    }

    @DeleteMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribe(@RequestParam String email) {
        NewsletterEmails newsletterEmail = newsletterEmailsRepository.findByEmail(String.valueOf(email));
        if (newsletterEmail != null) {
            newsletterEmailsRepository.delete(newsletterEmail);
            return ResponseEntity.ok("You have been unsubscribed successfully.");
        } else {
            return ResponseEntity.ok("The provided email is not subscribed to our Newsletter.");
        }
    }

}
