package ru.zubkoff;

import java.net.URISyntaxException;

public class Main {
  public static void main(String[] args) {
    var client = new MyHttpClient();
    var console = System.console();
    while (true) {
      var targetUrl = console.readLine("Введите запрашиваемый ресурс: ");
      try {
        System.out.println(client.readContent(targetUrl));
      } catch (URISyntaxException e) {
        System.out.println("Неверный формат реусрса");
      } catch (UnavailableResourceException e) {
        System.out.println("Ресурс вернул код отличный от 200: " + e.getErrorStatusCode());
      }
    }
  }
}
