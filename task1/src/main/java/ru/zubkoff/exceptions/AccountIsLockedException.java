package ru.zubkoff.exceptions;

import java.time.Instant;

public class AccountIsLockedException extends RuntimeException {

  private final Instant lockedUntil;
  private final Instant throwedAt;

  public AccountIsLockedException(Instant lockedUntil, Instant throwedAt) {
    super("Lock will expire in " + (lockedUntil.getEpochSecond() - throwedAt.getEpochSecond()) + " seconds");
    this.lockedUntil = lockedUntil;
    this.throwedAt = throwedAt;
  }

  public Instant getThrowedAt() {
    return throwedAt;
  }
  
  public Instant getLockedUntil() {
    return lockedUntil;
  }

  
}
