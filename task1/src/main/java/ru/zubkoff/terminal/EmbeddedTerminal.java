package ru.zubkoff.terminal;

import ru.zubkoff.exceptions.InvalidPinException;
import ru.zubkoff.exceptions.UnauthorizedTerminalUsageException;
import ru.zubkoff.server.AccessToken;
import ru.zubkoff.server.TerminalServer;

public class EmbeddedTerminal implements Terminal {

  private final long accountId;
  private final PinValidator pinValidator;
  private final TerminalServer server;
  private AccessToken accessToken;

  public EmbeddedTerminal(long accountId, PinValidator pinValidator, TerminalServer server) {
    this.accountId = accountId;
    this.pinValidator = pinValidator;
    this.server = server;
  }

  @Override
  public void login(String pin) {
    if (pinValidator.isValidPin(pin)) {
      accessToken = server.login(accountId, pin);
    } else {
      throw new InvalidPinException(pin);
    }
  }

  @Override
  public void updateBalance(double diff) {
    if (accessToken == null) {
      throw new UnauthorizedTerminalUsageException();
    }
    server.updateBalance(accessToken, diff);
  }

  @Override
  public double getBalance() {
    if (accessToken == null) {
      throw new UnauthorizedTerminalUsageException();
    }
    return server.getBalance(accessToken);
  }

}
