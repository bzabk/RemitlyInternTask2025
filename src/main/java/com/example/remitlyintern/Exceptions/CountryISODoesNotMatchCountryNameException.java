package com.example.remitlyintern.Exceptions;

public class CountryISODoesNotMatchCountryNameException extends RuntimeException {
    public CountryISODoesNotMatchCountryNameException(String message) {
        super(message);
    }
}
