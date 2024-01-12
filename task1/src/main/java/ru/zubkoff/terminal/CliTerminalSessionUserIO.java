package ru.zubkoff.terminal;

import java.io.Console;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CliTerminalSessionUserIO implements TerminalSessionUserIO {

  private static final List<TerminalOperation> OPERATIONS = Arrays.asList(TerminalOperation.values());
  private static final String WRONG_FORMAT_INPUT_WARN = "Неверный формат ввода";
  private static final int PIN_LENGTH = 4;

  private final Console console;

  public CliTerminalSessionUserIO(Console console) {
    this.console = console;
  }

  @Override
  public String readPin() {
    StringBuilder pin = new StringBuilder();
    while (pin.length() < PIN_LENGTH) {
      var input = console.readLine("Введите следующий символ пин-кода: ");
      if(input.length() == 1 && Character.isDigit(input.charAt(0))) {
        pin.append(input);
      } else {
        System.out.println(WRONG_FORMAT_INPUT_WARN);
      }
    }
    return pin.toString();
  }

  @Override
  public TerminalOperation readTerminalOperation() {
    System.out.println(IntStream.range(0, OPERATIONS.size())
      .mapToObj(operationIndex -> operationIndex + " - " + OPERATIONS.get(operationIndex).getDescription())
      .collect(Collectors.joining("\n")));
    Integer value = null;
    while (value == null) {
      try {
        value = Integer.parseInt(console.readLine("Введите номер действия: "));
      } catch (NumberFormatException e) {
        System.out.println(WRONG_FORMAT_INPUT_WARN);
        continue;
      }
      if(value < 0 || OPERATIONS.size() <= value) {
        value = null;
        System.out.println("Команды с таким номером не существует");
      }
    }
    return OPERATIONS.get(value);
  }

  @Override
  public void showBalance(double balance) {
    System.out.println("Баланс: " + balance);
  }

  @Override
  public double readMoney() {
    Double value = null;
    while (value == null) {
      try {
        value = Double.parseDouble(console.readLine("Введите сумму:"));
      } catch (NumberFormatException e) {
        System.out.println(WRONG_FORMAT_INPUT_WARN);
        continue;
      }
      if(value <= 0) {
        value = null;
        System.out.println("Сумма должна быть > 0");
      }
    }
    return value;
  }

  @Override
  public void warnForInvalidPin() {    
    System.out.println("Введенный пин-код не соответствует формату");
  }

  @Override
  public void warnForWrongCredentials() {
    System.out.println("Неверный id или пин-код");
  }

  @Override
  public void warnForAccessingLockedAccount(long secondsUntilUnlock) {    
    System.out.println("Аккаунт заблокирован, повторите через " + secondsUntilUnlock + " секунд");
  }

  @Override
  public void warnForFailedBalanceManipulation() {   
    System.out.println("Операция не может быть выполнена");
  }

  @Override
  public void warnForInvalidAccessToken() {
    System.out.println("Доступ к аккаунту невозможен, авторизируйтесь заново");
  }

}
