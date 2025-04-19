package com.example.remitlyintern.Exceptions;

public class CountryISODoesNotExistException extends RuntimeException {
    public CountryISODoesNotExistException(String message) {
        super(message);
    }
}
