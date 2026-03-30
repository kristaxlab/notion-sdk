package io.kristixlab.notion.api.http;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.base.client.HttpClient;
import io.kristixlab.notion.api.http.base.client.HttpClient.HttpRequest;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NotionVersionInterceptorTest {

  @Test
  @DisplayName("beforeSend adds Notion-Version header")
  void beforeSend_addsNotionVersionHeader() {
    NotionVersionInterceptor interceptor = new NotionVersionInterceptor("2026-03-11");
    HttpRequest req =
        HttpRequest.builder()
            .url("https://api.notion.com/v1/pages")
            .method(HttpClient.HttpMethod.GET)
            .headers(Map.of())
            .build();
    HttpRequest result = interceptor.beforeSend(req);
    assertEquals("2026-03-11", result.headers().get("Notion-Version"));
  }

  @Test
  @DisplayName("beforeSend overwrites existing Notion-Version header")
  void beforeSend_overwritesExistingNotionVersion() {
    NotionVersionInterceptor interceptor = new NotionVersionInterceptor("2026-03-11");
    HttpRequest req =
        HttpRequest.builder()
            .url("https://api.notion.com/v1/pages")
            .method(HttpClient.HttpMethod.GET)
            .headers(Map.of("Notion-Version", "old-version"))
            .build();
    HttpRequest result = interceptor.beforeSend(req);
    assertEquals("2026-03-11", result.headers().get("Notion-Version"));
  }

  @Test
  @DisplayName("beforeSend preserves other headers")
  void beforeSend_preservesOtherHeaders() {
    NotionVersionInterceptor interceptor = new NotionVersionInterceptor("2026-03-11");
    HttpRequest req =
        HttpRequest.builder()
            .url("https://api.notion.com/v1/pages")
            .method(HttpClient.HttpMethod.GET)
            .headers(Map.of("X-Custom", "foo"))
            .build();
    HttpRequest result = interceptor.beforeSend(req);
    assertEquals("foo", result.headers().get("X-Custom"));
    assertEquals("2026-03-11", result.headers().get("Notion-Version"));
  }
}
