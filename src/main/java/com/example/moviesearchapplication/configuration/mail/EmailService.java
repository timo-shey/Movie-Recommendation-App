package com.example.moviesearchapplication.configuration.mail;

public interface EmailService {
    void sendEmail(String to, String subject, String message);
}
