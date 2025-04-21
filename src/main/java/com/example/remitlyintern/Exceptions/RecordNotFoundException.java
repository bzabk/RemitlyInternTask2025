package com.example.remitlyintern.Exceptions;

public class RecordNotFoundException extends RuntimeException {
    /**
     * Exception is thrown when a record is not found in the database.
     */
    public RecordNotFoundException(String message) {
        super(message);
    }
}
