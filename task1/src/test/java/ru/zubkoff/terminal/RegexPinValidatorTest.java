package ru.zubkoff.terminal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;


public class RegexPinValidatorTest {
  
  RegexPinValidator validator = new RegexPinValidator();

  @Test
  @DisplayName("Подходят пинкоды только формата 4-х чисел")
  public void discardsInvalidPinsTest() {
    assertFalse(validator.isValidPin("12345"));
    assertFalse(validator.isValidPin("123"));
    assertFalse(validator.isValidPin("abcd"));
    assertFalse(validator.isValidPin("123a"));

    assertTrue(validator.isValidPin("0000"));
    assertTrue(validator.isValidPin("1234"));
  }

}
