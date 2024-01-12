package ru.zubkoff.terminal;

public interface Terminal {

  void updateBalance(double diff);

  double getBalance();

  void login(String pin);
  
}
