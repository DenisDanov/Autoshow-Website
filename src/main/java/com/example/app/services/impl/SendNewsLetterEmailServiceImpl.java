package com.example.app.services.impl;

import com.example.app.data.entities.NewsletterEmails;
import com.example.app.services.NewsletterEmailsService;
import com.example.app.services.SendGridEmailService;
import com.example.app.services.SendNewLetterEmailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Service
public class SendNewsLetterEmailServiceImpl implements SendNewLetterEmailsService {
    private final SendGridEmailService sendGridEmailService;
    private final NewsletterEmailsService newsletterEmailsService;

    @Autowired
    public SendNewsLetterEmailServiceImpl(SendGridEmailService sendGridEmailService,
                                          NewsletterEmailsService newsletterEmailsService) {
        this.sendGridEmailService = sendGridEmailService;
        this.newsletterEmailsService = newsletterEmailsService;
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    public void sendWeeklyEmails() throws IOException {
        // Get the list of subscribed emails
        List<NewsletterEmails> subscribedEmails = newsletterEmailsService.findAll();

        // Calculate the current date
        Instant currentDate = Instant.now();

        // Iterate over the subscribed emails and send emails every two weeks
        for (NewsletterEmails email : subscribedEmails) {
            Instant lastSentDate = email.getDateOfLastEmailSend();

            // Check if two weeks have passed since the last email
            if (lastSentDate == null || lastSentDate.plusSeconds(2 * 7 * 24 * 60 * 60)
                    .isBefore(currentDate)) {
                // Send the email
                sendGridEmailService.sendWeeklyNewsletter(email.getEmail(),"Weekly Newsletter");

                // Update the last sent date in the database
                email.setDateOfLastEmailSend(currentDate);
                newsletterEmailsService.save(email);
            }
        }
    }
}
