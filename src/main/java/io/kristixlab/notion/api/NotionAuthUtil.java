package io.kristixlab.notion.api;

import java.util.Base64;

public class NotionAuthUtil {

  public static String bearer(String token) {
    return "Bearer " + token;
  }

  public static String basic(String clientId, String clientSecret) {
    String token = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
    return "Basic " + token;
  }
}
