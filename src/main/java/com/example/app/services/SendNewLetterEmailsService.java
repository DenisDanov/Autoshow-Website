package com.example.app.services;

import org.springframework.stereotype.Service;

import java.io.IOException;


public interface SendNewLetterEmailsService {

    void sendWeeklyEmails() throws IOException;

}
