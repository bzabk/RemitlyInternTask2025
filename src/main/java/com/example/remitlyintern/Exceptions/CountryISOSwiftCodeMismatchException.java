package com.example.remitlyintern.Exceptions;

public class CountryISOSwiftCodeMismatchException extends CountryISOException {
    /**
     * Exception is thrown when 5's and 6's sign in SwiftCode does not match with CountryISO2 code
     *
     */
    public CountryISOSwiftCodeMismatchException(String message) {
        super(message);
    }
}
