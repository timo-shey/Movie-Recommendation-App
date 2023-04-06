package com.example.moviesearchapplication.exception;

public class MailSendingException extends RuntimeException{
    public MailSendingException(String errorMessage) {
        super(errorMessage);
    }

}
