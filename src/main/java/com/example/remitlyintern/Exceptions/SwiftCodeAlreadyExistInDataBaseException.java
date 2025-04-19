package com.example.remitlyintern.Exceptions;

public class SwiftCodeAlreadyExistInDataBaseException extends RuntimeException {
  public SwiftCodeAlreadyExistInDataBaseException(String message) {
    super(message);
  }
}
