package ru.zubkoff.terminal;

public class RegexPinValidator implements PinValidator {

  @Override
  public boolean isValidPin(String pin) {
    return pin.matches("^\\d{4}$");
  }
  
}
