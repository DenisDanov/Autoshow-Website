package com.example.app.services;

import com.example.app.data.entities.NewsletterEmails;

import java.util.List;

public interface NewsletterEmailsService {

    NewsletterEmails findByEmail(String email);

    void save(NewsletterEmails newsletterEmail);

    void delete(NewsletterEmails newsletterEmail);

    List<NewsletterEmails> findAll();
}
