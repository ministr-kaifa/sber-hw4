package ru.zubkoff;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class MyHttpClient {
  public String readContent(String url) throws URISyntaxException {
    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response;
    try {
      response = client.send(HttpRequest.newBuilder(new URI(url))
        .GET()
        .build(), BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }

    if(response.statusCode() != 200) {
      throw new UnavailableResourceException(response.statusCode());
    }
    return """
        response:
          status code: %d
          response body snippet: %s
        """.formatted(response.statusCode(), response.body().substring(0, 20) + "...");
  }
}
