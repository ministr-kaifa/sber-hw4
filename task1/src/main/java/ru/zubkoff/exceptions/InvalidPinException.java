package ru.zubkoff.exceptions;

public class InvalidPinException extends RuntimeException {

  public InvalidPinException(String accountPin) {
    super(accountPin + " is not valid pin");
  }
  
}
