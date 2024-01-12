package ru.zubkoff.terminal;

public interface TerminalSessionUserIO {

  String readPin();

  TerminalOperation readTerminalOperation();

  void showBalance(double balance);

  double readMoney();

  void warnForInvalidPin();

  void warnForWrongCredentials();

  void warnForAccessingLockedAccount(long secondsUntilUnlock);

  void warnForFailedBalanceManipulation();

  void warnForInvalidAccessToken();
  
}
