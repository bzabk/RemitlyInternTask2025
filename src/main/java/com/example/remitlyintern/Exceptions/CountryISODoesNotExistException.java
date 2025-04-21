package com.example.remitlyintern.Exceptions;

public class CountryISODoesNotExistException extends CountryISOException {
    /**
     * Exception is thrown when provided CountryISO code is not in Hashmap keys from CountryISO2Map class
     */
    public CountryISODoesNotExistException(String message) {
        super(message);
    }
}
