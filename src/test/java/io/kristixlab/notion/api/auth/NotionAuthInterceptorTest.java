package io.kristixlab.notion.api.auth;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.base.client.HttpClient;
import io.kristixlab.notion.api.http.base.client.HttpClient.HttpRequest;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NotionAuthInterceptorTest {

  @Test
  @DisplayName("beforeSend adds Authorization if missing")
  void beforeSend_addsAuthorizationIfMissing() {
    NotionAuthInterceptor interceptor =
        new NotionAuthInterceptor(new FixedTokenProvider("test-token"));
    HttpRequest req =
        HttpRequest.builder()
            .url("https://api.notion.com/v1/pages")
            .method(HttpClient.HttpMethod.GET)
            .headers(Map.of())
            .build();
    HttpRequest result = interceptor.beforeSend(req);
    assertEquals("Bearer test-token", result.headers().get("Authorization"));
  }

  @Test
  @DisplayName("beforeSend does not overwrite existing Authorization header")
  void beforeSend_doesNotOverwriteAuthorization() {
    NotionAuthInterceptor interceptor =
        new NotionAuthInterceptor(new FixedTokenProvider("Bearer test-token"));
    HttpRequest req =
        HttpRequest.builder()
            .url("https://api.notion.com/v1/pages")
            .method(HttpClient.HttpMethod.GET)
            .headers(Map.of("Authorization", "Basic abc123"))
            .build();
    HttpRequest result = interceptor.beforeSend(req);
    assertEquals("Basic abc123", result.headers().get("Authorization"));
  }

  @Test
  @DisplayName("beforeSend preserves other headers")
  void beforeSend_preservesOtherHeaders() {
    NotionAuthInterceptor interceptor =
        new NotionAuthInterceptor(new FixedTokenProvider("test-token"));
    HttpRequest req =
        HttpRequest.builder()
            .url("https://api.notion.com/v1/pages")
            .method(HttpClient.HttpMethod.GET)
            .headers(Map.of("X-Custom", "foo"))
            .build();
    HttpRequest result = interceptor.beforeSend(req);
    assertEquals("foo", result.headers().get("X-Custom"));
    assertEquals("Bearer test-token", result.headers().get("Authorization"));
  }
}
