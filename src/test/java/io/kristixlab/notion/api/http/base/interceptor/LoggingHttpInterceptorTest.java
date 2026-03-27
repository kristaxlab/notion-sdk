package io.kristixlab.notion.api.http.base.interceptor;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.base.client.HttpClient.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.*;

/** Unit tests for {@link LoggingHttpInterceptor}. */
class LoggingHttpInterceptorTest {

  private static final LoggingHttpInterceptor INTERCEPTOR = new LoggingHttpInterceptor("Test");

  private static HttpRequest get(String url) {
    return HttpRequest.builder().url(url).method(HttpMethod.GET).build();
  }

  private static HttpResponse responseOf(int status) {
    return new HttpResponse(status, Map.of(), "{\"ok\":true}".getBytes());
  }

  @Test
  @DisplayName("beforeSend returns the request instance unchanged")
  void beforeSend_returnsRequestUnchanged() {
    HttpRequest request = get("https://api.example.com/v1/pages");

    HttpRequest result = INTERCEPTOR.beforeSend(request);

    assertSame(request, result);
  }

  @Test
  @DisplayName("beforeSend does not throw for any body type")
  void beforeSend_doesNotThrowForAnyBody() {
    for (Body body : allBodies()) {
      HttpRequest request =
          HttpRequest.builder()
              .url("https://api.example.com/v1/x")
              .method(HttpMethod.POST)
              .body(body)
              .build();

      assertDoesNotThrow(() -> INTERCEPTOR.beforeSend(request), "body type: " + body);
    }
  }

  @Test
  @DisplayName("beforeSend does not throw for null body")
  void beforeSend_doesNotThrowForNullBody() {
    HttpRequest request =
        HttpRequest.builder().url("https://api.example.com/v1/x").method(HttpMethod.GET).build();

    assertDoesNotThrow(() -> INTERCEPTOR.beforeSend(request));
  }

  @Test
  @DisplayName("afterReceive does not throw for 2xx response")
  void afterReceive_doesNotThrowFor2xx() {
    HttpRequest req = get("https://api.example.com/v1/pages");

    assertDoesNotThrow(() -> INTERCEPTOR.afterReceive(req, responseOf(200)));
    assertDoesNotThrow(() -> INTERCEPTOR.afterReceive(req, responseOf(201)));
    assertDoesNotThrow(() -> INTERCEPTOR.afterReceive(req, responseOf(204)));
  }

  @Test
  @DisplayName("afterReceive does not throw for 4xx response (logs WARN, never propagates)")
  void afterReceive_doesNotThrowFor4xx() {
    HttpRequest req = get("https://api.example.com/v1/pages");

    assertDoesNotThrow(() -> INTERCEPTOR.afterReceive(req, responseOf(400)));
    assertDoesNotThrow(() -> INTERCEPTOR.afterReceive(req, responseOf(401)));
    assertDoesNotThrow(() -> INTERCEPTOR.afterReceive(req, responseOf(404)));
    assertDoesNotThrow(() -> INTERCEPTOR.afterReceive(req, responseOf(429)));
  }

  @Test
  @DisplayName("afterReceive does not throw for 5xx response")
  void afterReceive_doesNotThrowFor5xx() {
    HttpRequest req = get("https://api.example.com/v1/pages");

    assertDoesNotThrow(() -> INTERCEPTOR.afterReceive(req, responseOf(500)));
    assertDoesNotThrow(() -> INTERCEPTOR.afterReceive(req, responseOf(503)));
  }

  @Test
  @DisplayName("afterReceive handles response with null body bytes gracefully")
  void afterReceive_nullBodyBytes_doesNotThrow() {
    HttpRequest req = get("https://api.example.com/v1/pages");
    HttpResponse response = new HttpResponse(200, Map.of(), null);

    assertDoesNotThrow(() -> INTERCEPTOR.afterReceive(req, response));
  }

  @Test
  @DisplayName("beforeSend handles every Body subtype without throwing")
  void beforeSend_allBodySubtypes() {
    List<Body> bodies = allBodies();
    for (Body body : bodies) {
      HttpRequest req =
          HttpRequest.builder()
              .url("https://api.example.com/v1/x")
              .method(HttpMethod.POST)
              .body(body)
              .build();

      assertDoesNotThrow(
          () -> INTERCEPTOR.beforeSend(req), "failed for body: " + body.getClass().getSimpleName());
    }
  }

  private static List<Body> allBodies() {
    return List.of(
        new EmptyBody(),
        new StringBody("{}", "application/json"),
        new BytesBody(new byte[] {1, 2, 3}, "application/octet-stream"),
        new FileBody(new File("test.txt"), "text/plain"),
        new InputStreamBody(
            new ByteArrayInputStream(new byte[] {1}), 1, "application/octet-stream"),
        new MultipartBody(List.of(new TextPart("key", "val"))));
  }
}
