package ru.zubkoff.terminal;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import ru.zubkoff.server.Account;
import ru.zubkoff.server.TerminalServer;

public class UserSessionalTerminalTest {
  
  final String WRONG_PIN = "0000";
  final String CORRECT_PIN = "1111";

  TerminalServer server;

  @Before
  @BeforeEach
  public void reloadServer() {
    server = new TerminalServer(List.of(new Account(1, CORRECT_PIN, 10_000)));
  }

  @Test
  @DisplayName("Выводится предупреждение о неверном формате введенного пин-кода")
  public void wrongPinFormatTest() {
    var useriomock = mock(TerminalSessionUserIO.class);
    var terminal = new UserSessionalTerminal(1, useriomock, new RegexPinValidator(), server);
    String[] invalidPins = {
      "12345",
      "123",
      "abcd",
      "123b"
    };
    when(useriomock.readPin()).thenReturn(invalidPins[0], Arrays.copyOfRange(invalidPins, 1, invalidPins.length));
    for (int i = 0; i < invalidPins.length; i++) {
      terminal.startNewUserSession();
    }
    verify(useriomock, times(4)).warnForInvalidPin();
  }
  
  @Test
  @DisplayName("После 3-х неуспешных, из-за ввода неверного пин-кода, сессий счет блокируется, с соответствующими предупреждениями")
  public void lockWarnsTest() {
    var useriomock = mock(TerminalSessionUserIO.class);
    var terminal = new UserSessionalTerminal(1, useriomock, new RegexPinValidator(), server);
    when(useriomock.readPin()).thenReturn(WRONG_PIN);
    int unsuccessfulTriesBeforeLock = 3;
    for (int i = 0; i < unsuccessfulTriesBeforeLock; i++) {
      terminal.startNewUserSession();
    }
    verify(useriomock, times(2)).warnForWrongCredentials();
    verify(useriomock, times(1)).warnForAccessingLockedAccount(anyLong());
  }
  
  @Test
  @DisplayName("После 10 секунд блокировки, счет доступен для использования")
  public void unlockedAccountIsAvailableAfterLockTimeTest() throws InterruptedException {
    var useriomock = mock(TerminalSessionUserIO.class);
    var terminal = new UserSessionalTerminal(1, useriomock, new RegexPinValidator(), server);
    when(useriomock.readPin()).thenReturn(WRONG_PIN);
    int unsuccessfulTriesBeforeLock = 3;
    for (int i = 0; i < unsuccessfulTriesBeforeLock; i++) {
      terminal.startNewUserSession();
    }
    verify(useriomock, times(1)).warnForAccessingLockedAccount(anyLong());

    Thread.sleep(10_000);

    when(useriomock.readPin()).thenReturn(CORRECT_PIN);
    when(useriomock.readTerminalOperation()).thenReturn(TerminalOperation.EXIT);
    terminal.startNewUserSession();
    verify(useriomock, times(1)).warnForAccessingLockedAccount(anyLong());
  }

  @Test
  @DisplayName("Выводится предупреждение о том что изменение баланса суммой не делящейся на 100 невозможно")
  public void failedBalanceManipulationWarnsTest1() {
    var useriomock = mock(TerminalSessionUserIO.class);
    var terminal = new UserSessionalTerminal(1, useriomock, new RegexPinValidator(), server);
    when(useriomock.readPin()).thenReturn(CORRECT_PIN);
    when(useriomock.readTerminalOperation()).thenReturn(
      TerminalOperation.PUT_MONEY, 
      TerminalOperation.TAKE_MONEY, 
      TerminalOperation.EXIT);

    when(useriomock.readMoney()).thenReturn(1_001.);
    terminal.startNewUserSession();
    verify(useriomock, times(2)).warnForFailedBalanceManipulation();
  }

  @Test
  @DisplayName("Выводится предупреждение о том что отрицательное изменение баланса суммой больше чем на счете невозможно")
  public void failedBalanceManipulationWarnsTest2() {
    var useriomock = mock(TerminalSessionUserIO.class);
    var terminal = new UserSessionalTerminal(1, useriomock, new RegexPinValidator(), server);
    when(useriomock.readPin()).thenReturn(CORRECT_PIN);
    when(useriomock.readTerminalOperation()).thenReturn(
      TerminalOperation.TAKE_MONEY, 
      TerminalOperation.EXIT);

    when(useriomock.readMoney()).thenReturn(100_000.);
    terminal.startNewUserSession();
    verify(useriomock, times(1)).warnForFailedBalanceManipulation();
  }

}
