package com.example.app.data.repositories;

import com.example.app.data.entities.NewsletterEmails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsletterEmailsRepository extends JpaRepository<NewsletterEmails, Long> {

    NewsletterEmails findByEmail(String email);
}
