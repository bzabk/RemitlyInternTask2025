package com.example.remitlyintern.Exceptions;

public class CountryISODoesNotMatchWithSwiftCodeException extends RuntimeException {
    public CountryISODoesNotMatchWithSwiftCodeException(String message) {
        super(message);
    }
}
