package com.example.remitlyintern.Exceptions;

public class SwiftCodeAlreadyExistInDataBaseException extends RuntimeException {
  /**
   * Exception is thrown when user tries to Post data with swiftCode that already exists in database
   */
  public SwiftCodeAlreadyExistInDataBaseException(String message) {
    super(message);
  }
}
