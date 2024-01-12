package ru.zubkoff;

import java.util.List;

import ru.zubkoff.server.Account;
import ru.zubkoff.server.TerminalServer;
import ru.zubkoff.terminal.CliTerminalSessionUserIO;
import ru.zubkoff.terminal.RegexPinValidator;
import ru.zubkoff.terminal.UserSessionalTerminal;

public class Main {
  public static void main(String[] args) {
    var server = new TerminalServer(List.of(
      new Account(1, "1111", 10_000),
      new Account(2, "2222", 10_000),
      new Account(3, "3333", 10_000),
      new Account(4, "4444", 10_000),
      new Account(5, "5555", 10_000)));

    var terminal = new UserSessionalTerminal(
      1,
      new CliTerminalSessionUserIO(System.console()),
      new RegexPinValidator(),
      server);
    
    while (true) {
      terminal.startNewUserSession();
    }

  }
}