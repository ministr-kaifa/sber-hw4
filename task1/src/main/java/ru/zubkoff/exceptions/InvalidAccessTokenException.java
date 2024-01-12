package ru.zubkoff.exceptions;

public class InvalidAccessTokenException extends RuntimeException {
  public InvalidAccessTokenException() {
    super("Something vent wrong, try relogin");
  }
}
