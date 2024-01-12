package ru.zubkoff.terminal;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import ru.zubkoff.exceptions.InvalidPinException;
import ru.zubkoff.exceptions.UnauthorizedTerminalUsageException;
import ru.zubkoff.server.Account;
import ru.zubkoff.server.TerminalServer;

public class EmbeddedTerminalTest {

  final String WRONG_PIN = "0000";  
  final String CORRECT_PIN = "1111";
  
  TerminalServer server;
  EmbeddedTerminal terminal;

  @Before
  @BeforeEach
  public void reloadServer() {
    server = new TerminalServer(List.of(new Account(1, CORRECT_PIN, 10_000)));
    terminal = new EmbeddedTerminal(1, new RegexPinValidator(), server);
  }

  @Test
  @DisplayName("Невозможно использовать терминал не авторизировавшись")
  public void unauthorizedUsageTest() {
    assertThrows(UnauthorizedTerminalUsageException.class, () -> terminal.updateBalance(100));
    assertThrows(UnauthorizedTerminalUsageException.class, () -> terminal.getBalance());
    terminal.login(CORRECT_PIN);
    assertDoesNotThrow(() -> terminal.updateBalance(100));
    assertDoesNotThrow(() -> terminal.getBalance());
  }

  @Test
  @DisplayName("При вводе пин-кода неверного формата выбрасывается InvalidPinException")
  public void discardsInvalidPinsTest() {
    assertThrows(InvalidPinException.class, () -> terminal.login("12345"));
    assertThrows(InvalidPinException.class, () -> terminal.login("123"));
    assertThrows(InvalidPinException.class, () -> terminal.login("abcd"));
    assertThrows(InvalidPinException.class, () -> terminal.login("12b4"));
  }

}
