package ru.zubkoff.server;

public class AccessToken {
  
  private final long accountId;

  public long getAccountId() {
    return accountId;
  }

  public AccessToken(long accountId) {
    this.accountId = accountId;
  }
}
