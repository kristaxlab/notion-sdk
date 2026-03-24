package io.kristixlab.notion.api.http.transport;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.client.HttpClient;
import io.kristixlab.notion.api.http.client.HttpClient.*;
import io.kristixlab.notion.api.http.interceptor.HttpClientInterceptor;
import io.kristixlab.notion.api.http.client.InterceptingHttpClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.*;

/** Unit tests for {@link InterceptingHttpClient}. */
class InterceptingHttpClientTest {

  // ------------------------------------------------------------------
  // Helpers
  // ------------------------------------------------------------------

  /** A fake HttpClient that records the request it received and returns a fixed response. */
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

  /** Records invocation order and can optionally add a header in beforeSend. */
  private static class SpyInterceptor implements HttpClientInterceptor {
    final String id;
    final List<String> log;
    final String headerToAdd;

    SpyInterceptor(String id, List<String> log) {
      this(id, log, null);
    }

    SpyInterceptor(String id, List<String> log, String headerToAdd) {
      this.id = id;
      this.log = log;
      this.headerToAdd = headerToAdd;
    }

    @Override
    public HttpRequest beforeSend(HttpRequest request) {
      log.add(id + ":before");
      if (headerToAdd != null) {
        return request.toBuilder().header("X-" + id, headerToAdd).build();
      }
      return request;
    }

    @Override
    public void afterReceive(HttpRequest request, HttpResponse response) {
      log.add(id + ":after");
    }
  }

  private static final HttpResponse OK_RESPONSE = new HttpResponse(200, Map.of(), "{}".getBytes());

  private static HttpRequest simpleGet(String url) {
    return HttpRequest.builder().url(url).method(HttpMethod.GET).build();
  }

  // ------------------------------------------------------------------
  // Tests
  // ------------------------------------------------------------------

  @Test
  @DisplayName("Delegates to wrapped client when no interceptors")
  void noInterceptors() throws IOException {
    var fake = new FakeHttpClient(OK_RESPONSE);
    var client = new InterceptingHttpClient(fake, List.of());

    HttpResponse rs = client.send(simpleGet("https://example.com"));

    assertSame(OK_RESPONSE, rs);
    assertEquals("https://example.com", fake.lastRequest.url());
  }

  @Test
  @DisplayName("Interceptors fire in order: beforeSend → delegate → afterReceive")
  void interceptorOrdering() throws IOException {
    var log = new ArrayList<String>();
    var fake = new FakeHttpClient(OK_RESPONSE);
    var client =
        new InterceptingHttpClient(
            fake, List.of(new SpyInterceptor("A", log), new SpyInterceptor("B", log)));

    client.send(simpleGet("https://example.com"));

    assertEquals(List.of("A:before", "B:before", "A:after", "B:after"), log);
  }

  @Test
  @DisplayName("beforeSend can modify the request (add header)")
  void beforeSendModifiesRequest() throws IOException {
    var log = new ArrayList<String>();
    var fake = new FakeHttpClient(OK_RESPONSE);
    var client =
        new InterceptingHttpClient(fake, List.of(new SpyInterceptor("Auth", log, "Bearer token")));

    client.send(simpleGet("https://example.com"));

    assertEquals("Bearer token", fake.lastRequest.headers().get("X-Auth"));
  }

  @Test
  @DisplayName("Chained beforeSend mutations accumulate")
  void chainedMutations() throws IOException {
    var log = new ArrayList<String>();
    var fake = new FakeHttpClient(OK_RESPONSE);
    var client =
        new InterceptingHttpClient(
            fake,
            List.of(new SpyInterceptor("First", log, "1"), new SpyInterceptor("Second", log, "2")));

    client.send(simpleGet("https://example.com"));

    assertEquals("1", fake.lastRequest.headers().get("X-First"));
    assertEquals("2", fake.lastRequest.headers().get("X-Second"));
  }

  @Test
  @DisplayName("beforeSend exception prevents request from being sent")
  void beforeSendThrows() {
    var fake = new FakeHttpClient(OK_RESPONSE);
    HttpClientInterceptor exploding =
        new HttpClientInterceptor() {
          @Override
          public HttpRequest beforeSend(HttpRequest request) {
            throw new RuntimeException("rate limited");
          }
        };
    var client = new InterceptingHttpClient(fake, List.of(exploding));

    assertThrows(RuntimeException.class, () -> client.send(simpleGet("https://example.com")));
    assertNull(fake.lastRequest, "Request should not have been sent");
  }

  @Test
  @DisplayName("afterReceive sees the modified request and the response")
  void afterReceiveSeesModifiedRequest() throws IOException {
    var captured =
        new Object() {
          HttpRequest request;
          HttpResponse response;
        };
    HttpClientInterceptor addHeader =
        new HttpClientInterceptor() {
          @Override
          public HttpRequest beforeSend(HttpRequest request) {
            return request.toBuilder().header("X-Trace", "abc").build();
          }

          @Override
          public void afterReceive(HttpRequest request, HttpResponse response) {
            captured.request = request;
            captured.response = response;
          }
        };
    var fake = new FakeHttpClient(OK_RESPONSE);
    var client = new InterceptingHttpClient(fake, List.of(addHeader));

    client.send(simpleGet("https://example.com"));

    assertEquals("abc", captured.request.headers().get("X-Trace"));
    assertSame(OK_RESPONSE, captured.response);
  }
}
