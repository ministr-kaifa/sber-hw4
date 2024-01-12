package ru.zubkoff.exceptions;

public class UnauthorizedTerminalUsageException extends RuntimeException {
  public UnauthorizedTerminalUsageException() {
    super("call .login() firstly");
  }
}
