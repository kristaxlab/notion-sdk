package io.kristixlab.notion.api.http.client;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.client.HttpClient.*;
import io.kristixlab.notion.api.http.interceptor.HttpClientInterceptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.*;

/** Unit tests for {@link ErrorHandlingHttpClient} and {@link ErrorResponseHandler}. */
class ErrorHandlingHttpClientTest {


  /** A fake HttpClient that returns a fixed response. */
  private static class FakeHttpClient implements HttpClient {
    HttpRequest lastRequest;
    final HttpResponse fixedResponse;

    FakeHttpClient(HttpResponse fixedResponse) {
      this.fixedResponse = fixedResponse;
    }

    @Override
    public HttpResponse send(HttpRequest request) {
      this.lastRequest = request;
      return fixedResponse;
    }
  }

  /** A simple domain exception for testing. */
  private static class TestApiException extends RuntimeException {
    final int status;
    final String body;

    TestApiException(int status, String body) {
      super("API error " + status + ": " + body);
      this.status = status;
      this.body = body;
    }
  }

  private static HttpRequest simpleGet(String url) {
    return HttpRequest.builder().url(url).method(HttpMethod.GET).build();
  }

  private static HttpResponse responseWithStatus(int status) {
    return new HttpResponse(status, Map.of(), "{}".getBytes());
  }

  private static HttpResponse responseWithStatus(int status, String body) {
    return new HttpResponse(status, Map.of(), body.getBytes());
  }


  @Test
  @DisplayName("Constructor rejects null delegate")
  void nullDelegateThrows() {
    assertThrows(
        NullPointerException.class,
        () -> new ErrorHandlingHttpClient(null, ErrorResponseHandler.QUIET));
  }

  @Test
  @DisplayName("Constructor rejects null errorHandler")
  void nullErrorHandlerThrows() {
    var fake = new FakeHttpClient(responseWithStatus(200));
    assertThrows(NullPointerException.class, () -> new ErrorHandlingHttpClient(fake, null));
  }

  @Test
  @DisplayName("NOOP handler passes through all responses, including errors")
  void noopHandlerPassesThrough() throws IOException {
    var fake = new FakeHttpClient(responseWithStatus(500, "Internal Server Error"));
    var client = new ErrorHandlingHttpClient(fake, ErrorResponseHandler.QUIET);

    HttpResponse rs = client.send(simpleGet("https://example.com"));

    assertEquals(500, rs.statusCode());
    assertEquals("Internal Server Error", rs.bodyAsString());
  }

  @Test
  @DisplayName("Successful response (2xx) passes through unchanged")
  void successfulResponsePassesThrough() throws IOException {
    var fake = new FakeHttpClient(responseWithStatus(200, "{\"ok\": true}"));
    ErrorResponseHandler handler =
        (req, res) -> {
          if (res.statusCode() >= 400) {
            throw new TestApiException(res.statusCode(), res.bodyAsString());
          }
        };
    var client = new ErrorHandlingHttpClient(fake, handler);

    HttpResponse rs = client.send(simpleGet("https://example.com"));

    assertEquals(200, rs.statusCode());
    assertEquals("{\"ok\": true}", rs.bodyAsString());
  }

  @Test
  @DisplayName("Error response (4xx) triggers handler exception")
  void clientErrorThrows() {
    var fake = new FakeHttpClient(responseWithStatus(400, "{\"code\": \"validation_error\"}"));
    ErrorResponseHandler handler =
        (req, res) -> {
          if (res.statusCode() >= 400) {
            throw new TestApiException(res.statusCode(), res.bodyAsString());
          }
        };
    var client = new ErrorHandlingHttpClient(fake, handler);

    TestApiException ex =
        assertThrows(TestApiException.class, () -> client.send(simpleGet("https://example.com")));
    assertEquals(400, ex.status);
    assertEquals("{\"code\": \"validation_error\"}", ex.body);
  }

  @Test
  @DisplayName("Error response (5xx) triggers handler exception")
  void serverErrorThrows() {
    var fake =
        new FakeHttpClient(responseWithStatus(503, "{\"message\": \"service unavailable\"}"));
    ErrorResponseHandler handler =
        (req, res) -> {
          if (res.statusCode() >= 400) {
            throw new TestApiException(res.statusCode(), res.bodyAsString());
          }
        };
    var client = new ErrorHandlingHttpClient(fake, handler);

    TestApiException ex =
        assertThrows(TestApiException.class, () -> client.send(simpleGet("https://example.com")));
    assertEquals(503, ex.status);
  }

  @Test
  @DisplayName("Handler receives the actual request (useful for error context)")
  void handlerReceivesRequest() {
    var fake = new FakeHttpClient(responseWithStatus(404));
    List<String> capturedUrls = new ArrayList<>();

    ErrorResponseHandler handler =
        (req, res) -> {
          capturedUrls.add(req.url());
          if (res.statusCode() >= 400) {
            throw new TestApiException(res.statusCode(), res.bodyAsString());
          }
        };
    var client = new ErrorHandlingHttpClient(fake, handler);

    assertThrows(
        TestApiException.class, () -> client.send(simpleGet("https://api.notion.com/v1/pages")));
    assertEquals(List.of("https://api.notion.com/v1/pages"), capturedUrls);
  }

  @Test
  @DisplayName("Composes with InterceptingHttpClient — logging fires before error handler throws")
  void composesWithInterceptingClient() {
    var fake = new FakeHttpClient(responseWithStatus(401, "{\"code\": \"unauthorized\"}"));
    List<String> log = new ArrayList<>();

    HttpClientInterceptor loggingInterceptor =
        new HttpClientInterceptor() {
          @Override
          public void afterReceive(HttpRequest request, HttpResponse response) {
            log.add("logged:" + response.statusCode());
          }
        };

    HttpClient intercepted = new InterceptingHttpClient(fake, List.of(loggingInterceptor));

    ErrorResponseHandler handler =
        (req, res) -> {
          if (res.statusCode() >= 400) {
            log.add("error:" + res.statusCode());
            throw new TestApiException(res.statusCode(), res.bodyAsString());
          }
        };
    HttpClient safe = new ErrorHandlingHttpClient(intercepted, handler);

    assertThrows(TestApiException.class, () -> safe.send(simpleGet("https://example.com")));

    assertEquals(List.of("logged:401", "error:401"), log);
  }

  @Test
  @DisplayName("IOException from delegate propagates without calling error handler")
  void ioExceptionPropagates() {
    HttpClient failing =
        request -> {
          throw new IOException("connection refused");
        };
    List<String> handlerLog = new ArrayList<>();
    ErrorResponseHandler handler = (req, res) -> handlerLog.add("should-not-run");
    var client = new ErrorHandlingHttpClient(failing, handler);

    assertThrows(IOException.class, () -> client.send(simpleGet("https://example.com")));
    assertTrue(handlerLog.isEmpty(), "Error handler should not run when IOException is thrown");
  }
}
