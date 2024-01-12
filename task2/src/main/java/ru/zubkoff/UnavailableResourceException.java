package ru.zubkoff;

public class UnavailableResourceException extends RuntimeException {
  private final int errorStatusCode;

  public int getErrorStatusCode() {
    return errorStatusCode;
  }

  public UnavailableResourceException(int errorStatusCode) {
    super("Requested resource returned code " + errorStatusCode);
    this.errorStatusCode = errorStatusCode;
  }
  
}
