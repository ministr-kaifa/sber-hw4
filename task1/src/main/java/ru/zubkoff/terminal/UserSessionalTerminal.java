package ru.zubkoff.terminal;

import ru.zubkoff.exceptions.AccountIsLockedException;
import ru.zubkoff.exceptions.FailedBalanceManipulationException;
import ru.zubkoff.exceptions.InvalidAccessTokenException;
import ru.zubkoff.exceptions.InvalidPinException;
import ru.zubkoff.exceptions.WrongCredentialsException;
import ru.zubkoff.server.TerminalServer;

public class UserSessionalTerminal extends EmbeddedTerminal {

  private final TerminalSessionUserIO userio;

  public UserSessionalTerminal(long accountId, TerminalSessionUserIO iomanager, PinValidator pinValidator,
      TerminalServer server) {
    super(accountId, pinValidator, server);
    this.userio = iomanager;
  }

  public void startNewUserSession() {
    var pin = userio.readPin();
    try {
      login(pin);
    } catch (InvalidPinException e) {
      userio.warnForInvalidPin();
      return;
    } catch (WrongCredentialsException e) {
      userio.warnForWrongCredentials();
      return;
    } catch (AccountIsLockedException e) {
      userio.warnForAccessingLockedAccount(e.getLockedUntil().getEpochSecond() - e.getThrowedAt().getEpochSecond());
      return;
    }

    while (true) {
      switch (userio.readTerminalOperation()) {
        case TAKE_MONEY -> {
          try {
            updateBalance(-userio.readMoney());
          } catch (FailedBalanceManipulationException e) {
            userio.warnForFailedBalanceManipulation();
          } catch (AccountIsLockedException e) {
            userio.warnForAccessingLockedAccount(e.getLockedUntil().getEpochSecond() - e.getThrowedAt().getEpochSecond());
          } catch (InvalidAccessTokenException e) {
            userio.warnForInvalidAccessToken();
          }
        }
        case PUT_MONEY -> {
          try {
            updateBalance(userio.readMoney());
          } catch (FailedBalanceManipulationException e) {
            userio.warnForFailedBalanceManipulation();
          } catch (AccountIsLockedException e) {
            userio.warnForAccessingLockedAccount(e.getLockedUntil().getEpochSecond() - e.getThrowedAt().getEpochSecond());
          } catch (InvalidAccessTokenException e) {
            userio.warnForInvalidAccessToken();
          }
        }
        case SHOW_BALANCE -> {
          try {
            userio.showBalance(getBalance());
          } catch (AccountIsLockedException e) {
            userio.warnForAccessingLockedAccount(e.getLockedUntil().getEpochSecond() - e.getThrowedAt().getEpochSecond());
          } catch (InvalidAccessTokenException e) {            
            userio.warnForInvalidAccessToken();
          }
        }
        case EXIT -> {
          return;
        }
      }
    }
    
  }

}
