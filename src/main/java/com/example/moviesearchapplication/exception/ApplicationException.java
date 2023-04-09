package com.example.moviesearchapplication.exception;

public class ApplicationException extends GenericRuntimeException{
    private static final String ERROR_CODE = "400";
    public ApplicationException(String message) {
        super(ERROR_CODE,message);
    }

}
