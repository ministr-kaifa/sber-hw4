package ru.zubkoff.server;

import java.time.Instant;
import java.util.Optional;

public class Account {

  private final long id;
  private String pin;
  private double balance;
  private Optional<Instant> lockedUntil;

  public Account(long id, String pin, double balance) {
    this.id = id;
    this.pin = pin;
    this.balance = balance;
    this.lockedUntil = Optional.empty();
  }

  public boolean isLocked() {
    lockedUntil = lockedUntil.filter(Instant.now()::isBefore);
    return lockedUntil.isPresent();
  }

  public void lock(Instant until) {
    lockedUntil = Optional.of(until);
  }
  
  public long getId() {
    return id;
  }
  
  public String getPin() {
    return pin;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }
  
  public double getBalance() {
    return balance;
  }
  
  public void setBalance(double money) {
    this.balance = money;
  }

  public Optional<Instant> getLockedUntil() {
    lockedUntil = lockedUntil.filter(Instant.now()::isBefore);
    return lockedUntil;
  }
}
