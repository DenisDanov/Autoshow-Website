package com.example.demo.emailApp.newsLetterEmails;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsletterEmailsRepository extends JpaRepository<NewsletterEmails, Long> {

    NewsletterEmails findByEmail(String email);
}
