package ru.zubkoff.terminal;

public enum TerminalOperation {
  
  SHOW_BALANCE("Показать баланс"), TAKE_MONEY("Снять деньги"), 
  PUT_MONEY("Положить деньги"), EXIT("Выход");

  final String description;

  TerminalOperation(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
  
}
