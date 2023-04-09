package com.example.moviesearchapplication.exception;

public class FilmNotFoundException extends GenericRuntimeException{
    private static final String ERROR_CODE = "404";
    public FilmNotFoundException(String message) {
        super(ERROR_CODE,message);
    }
}
