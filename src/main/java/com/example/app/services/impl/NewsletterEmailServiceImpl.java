package com.example.app.services.impl;

import com.example.app.data.entities.NewsletterEmails;
import com.example.app.data.repositories.NewsletterEmailsRepository;
import com.example.app.services.NewsletterEmailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsletterEmailServiceImpl implements NewsletterEmailsService {

    private final NewsletterEmailsRepository newsletterEmailsRepository;

    public NewsletterEmailServiceImpl(NewsletterEmailsRepository newsletterEmailsRepository) {
        this.newsletterEmailsRepository = newsletterEmailsRepository;
    }

    @Override
    public NewsletterEmails findByEmail(String email) {
        return this.newsletterEmailsRepository.findByEmail(email);
    }

    @Override
    public void save(NewsletterEmails newsletterEmail) {
        this.newsletterEmailsRepository.save(newsletterEmail);
    }

    @Override
    public void delete(NewsletterEmails newsletterEmail) {
        this.newsletterEmailsRepository.delete(newsletterEmail);
    }

    @Override
    public List<NewsletterEmails> findAll() {
        return this.newsletterEmailsRepository.findAll();
    }
}
