package com.example.moviesearchapplication.exception;

public class EmailNotFoundException extends GenericRuntimeException{
    private static final String ERROR_CODE = "404";
    public EmailNotFoundException(String message) {
        super(ERROR_CODE,message);
    }
}
