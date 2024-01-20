package com.example.demo.emailApp.newsLetterEmails;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.Instant;

@Entity
public class NewsletterEmails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private Instant dateOfLastEmailSended;

    public NewsletterEmails(Long id, String email) {
        this.id = id;
        this.email = email;
        this.dateOfLastEmailSended = Instant.now().minus(Duration.ofDays(14));
    }

    public NewsletterEmails(String email) {
        this.email = email;
        this.dateOfLastEmailSended = Instant.now().minus(Duration.ofDays(14));
    }

    public NewsletterEmails() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getDateOfLastEmailSended() {
        return dateOfLastEmailSended;
    }

    public void setDateOfLastEmailSended(Instant dateOfLastEmailSended) {
        this.dateOfLastEmailSended = dateOfLastEmailSended;
    }
}
