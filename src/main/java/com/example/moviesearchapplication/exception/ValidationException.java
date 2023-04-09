package com.example.moviesearchapplication.exception;

public class ValidationException extends GenericRuntimeException{
    private static final String ERROR_CODE = "400";
    public ValidationException(String message) {
        super(ERROR_CODE,message);
    }

}
