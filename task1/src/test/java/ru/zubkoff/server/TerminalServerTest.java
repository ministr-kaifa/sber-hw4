package ru.zubkoff.server;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import ru.zubkoff.exceptions.AccountIsLockedException;
import ru.zubkoff.exceptions.FailedBalanceManipulationException;
import ru.zubkoff.exceptions.WrongCredentialsException;

public class TerminalServerTest {

  final String WRONG_PIN = "0000";  
  final String CORRECT_PIN = "1111";

  TerminalServer server;

  @Before
  @BeforeEach
  public void reloadServer() {
    server = new TerminalServer(List.of(new Account(1, CORRECT_PIN, 10_000)));
  }
  
  @Test
  @DisplayName("На 3 неправильный ввод пинкода блокируется аккаунт")
  public void lockTest() {
    int unsuccessfulTriesBeforeLock = 3;
    for (int i = 0; i < unsuccessfulTriesBeforeLock - 1; i++) {
      assertThrows(WrongCredentialsException.class, () -> server.login(1, WRONG_PIN));
    }
    assertThrows(AccountIsLockedException.class, () -> server.login(1, WRONG_PIN));
  }
  
  @Test
  @DisplayName("По прошествии 10 секунд счет разблокируется")
  public void unlockTest() throws InterruptedException {
    int unsuccessfulTriesBeforeLock = 3;
    for (int i = 0; i < unsuccessfulTriesBeforeLock; i++) {
      try {
        server.login(1, WRONG_PIN);
      } catch (Exception ignore) {}
    }
    assertThrows(AccountIsLockedException.class, () -> server.login(1, CORRECT_PIN));
    Thread.sleep(10_000);
    assertDoesNotThrow(() -> server.login(1, CORRECT_PIN));
  }

  @Test
  @DisplayName("Изменение баланса суммой не делящейся на 100 невозможно")
  public void unperformableOperationsTest1() {
    assertThrows(FailedBalanceManipulationException.class, () -> server.updateBalance(server.login(1, CORRECT_PIN), 1_003));
  }

  @Test
  @DisplayName("Отрицательное изменение баланса, суммой больше чем на счете, невозможно")
  public void unperformableOperationsTest2() {
    assertThrows(FailedBalanceManipulationException.class, () -> server.updateBalance(server.login(1, CORRECT_PIN), -20_000));
  }
}
