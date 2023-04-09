package com.example.moviesearchapplication.exception;

public class InvalidCredentialsException extends GenericRuntimeException{
    private static final String ERROR_CODE = "400";
    public InvalidCredentialsException(String message) {
        super(ERROR_CODE,message);
    }

}
