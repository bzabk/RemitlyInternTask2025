package com.example.remitlyintern.Exceptions;

public class HeadquarterSwiftCodeMismatchException extends RuntimeException {
    /**
     * Exception is thrown when in PostMapping the user provides `false` in the `isHeadquarter` field,
     * but the provided SwiftCode indicates that the bank is a headquarters, or conversely, the user provides `true`,
     * but the SwiftCode indicates that the bank is a branch.
     */
    public HeadquarterSwiftCodeMismatchException(String message) {
        super(message);
    }
}
