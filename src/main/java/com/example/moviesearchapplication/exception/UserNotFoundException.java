package com.example.moviesearchapplication.exception;

public class UserNotFoundException extends GenericRuntimeException {
    private static final String ERROR_CODE = "404";
    public UserNotFoundException(String message) {
        super(ERROR_CODE,message);
    }

}
