package com.example.app.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;


public interface SendGridEmailService {

    void sendSimpleMessage(String to, String subject, String text) throws IOException;

    void sendAccountLockEmail(String email, Timestamp expireDate) throws IOException;

    void sendWeeklyNewsletter(String email, String subject) throws IOException;
}
