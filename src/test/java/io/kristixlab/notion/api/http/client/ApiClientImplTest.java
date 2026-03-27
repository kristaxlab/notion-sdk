package io.kristixlab.notion.api.http.client;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.client.HttpClient.*;
import io.kristixlab.notion.api.http.config.ApiClientConfig;
import io.kristixlab.notion.api.http.request.ApiPath;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.*;

/** Unit tests for {@link ApiClientImpl}. */
class ApiClientImplTest {

  private static class FakeHttpClient implements HttpClient {
    HttpRequest lastRequest;
    HttpResponse fixedResponse = new HttpResponse(200, Map.of(), "{}".getBytes());
    IOException toThrow;

    FakeHttpClient() {}

    FakeHttpClient(HttpResponse fixedResponse) {
      this.fixedResponse = fixedResponse;
    }

    @Override
    public HttpResponse send(HttpRequest request) throws IOException {
      this.lastRequest = request;
      if (toThrow != null) throw toThrow;
      return fixedResponse;
    }
  }

  private static HttpResponse json(String body) {
    return new HttpResponse(200, Map.of(), body.getBytes());
  }

  /** Minimal POJO for deserialization tests. */
  @lombok.Data
  static class Bean {
    private String name;
  }

  @Test
  @DisplayName("Constructor rejects null httpClient")
  void constructor_nullHttpClient_throws() {
    assertThrows(NullPointerException.class, () -> new ApiClientImpl(null, "https://example.com"));
  }

  @Test
  @DisplayName("Constructor rejects null baseUrl")
  void constructor_nullBaseUrl_throws() {
    assertThrows(NullPointerException.class, () -> new ApiClientImpl(new FakeHttpClient(), null));
  }

  @Test
  @DisplayName("Constructor strips trailing slash from baseUrl")
  void constructor_stripsTrailingSlash() {
    var fake = new FakeHttpClient();
    var client = new ApiClientImpl(fake, "https://example.com/v1/");

    client.call("GET", ApiPath.from("/pages"), String.class);

    assertEquals("https://example.com/v1/pages", fake.lastRequest.url());
  }

  @Test
  @DisplayName("Constructor preserves bare protocol strings such as https://")
  void constructor_preservesBareProtocol() {
    var fake = new FakeHttpClient();
    var client = new ApiClientImpl(fake, "https://");

    client.call("GET", ApiPath.from("example.com/v1"), String.class);

    assertTrue(fake.lastRequest.url().startsWith("https://"));
  }

  @Test
  @DisplayName("call() resolves path params and query params into the URL")
  void call_resolvesApiPath() {
    var fake = new FakeHttpClient();
    var client = new ApiClientImpl(fake, "https://api.example.com/v1");

    ApiPath path =
        ApiPath.builder("/pages/{page_id}")
            .pathParam("page_id", "abc-123")
            .queryParam("filter", "active")
            .build();

    client.call("GET", path, String.class);

    assertEquals("https://api.example.com/v1/pages/abc-123?filter=active", fake.lastRequest.url());
  }

  @Test
  @DisplayName("call() maps method string to correct HttpMethod enum")
  void call_setsHttpMethod() {
    var fake = new FakeHttpClient();
    var client = new ApiClientImpl(fake, "https://api.example.com/v1");

    for (String method : List.of("GET", "POST", "PATCH", "PUT", "DELETE")) {
      client.call(method, ApiPath.from("/x"), String.class);
      assertEquals(
          HttpClient.HttpMethod.valueOf(method), fake.lastRequest.method(), "method: " + method);
    }
  }

  @Test
  @DisplayName("call() is case-insensitive for method string")
  void call_methodIsCaseInsensitive() {
    var fake = new FakeHttpClient();
    var client = new ApiClientImpl(fake, "https://api.example.com/v1");

    client.call("get", ApiPath.from("/pages"), String.class);

    assertEquals(HttpMethod.GET, fake.lastRequest.method());
  }

  @Test
  @DisplayName("call() throws IllegalArgumentException for unknown HTTP method")
  void call_unknownMethod_throws() {
    var client = new ApiClientImpl(new FakeHttpClient(), "https://api.example.com/v1");

    assertThrows(
        IllegalArgumentException.class,
        () -> client.call("BREW", ApiPath.from("/pages"), String.class));
  }

  @Test
  @DisplayName("call() adds Content-Type: application/json for a plain object body")
  void call_plainObjectBody_addsJsonContentType() {
    var fake = new FakeHttpClient();
    var client = new ApiClientImpl(fake, "https://api.example.com/v1");

    client.call("POST", ApiPath.from("/pages"), Map.of("title", "Hello"), String.class);

    assertEquals("application/json", fake.lastRequest.headers().get("Content-Type"));
  }

  @Test
  @DisplayName("call() does NOT add Content-Type for a null body")
  void call_nullBody_noContentType() {
    var fake = new FakeHttpClient();
    var client = new ApiClientImpl(fake, "https://api.example.com/v1");

    client.call("GET", ApiPath.from("/pages"), null, String.class);

    assertFalse(fake.lastRequest.headers().containsKey("Content-Type"));
  }

  @Test
  @DisplayName("call() does NOT add Content-Type for a Body instance (e.g. MultipartBody)")
  void call_bodyInstance_noContentType() {
    var fake = new FakeHttpClient();
    var client = new ApiClientImpl(fake, "https://api.example.com/v1");

    MultipartBody body = new MultipartBody(List.of(new TextPart("key", "val")));
    client.call("POST", ApiPath.from("/upload"), body, String.class);

    assertFalse(fake.lastRequest.headers().containsKey("Content-Type"));
  }

  @Test
  @DisplayName("call() forwards extra headers to the request")
  void call_extraHeaders_forwarded() {
    var fake = new FakeHttpClient();
    var client = new ApiClientImpl(fake, "https://api.example.com/v1");

    Map<String, String> extra = Map.of("X-Custom-Header", "value123", "X-Other", "other");
    client.call("GET", ApiPath.from("/pages"), extra, null, String.class);

    assertEquals("value123", fake.lastRequest.headers().get("X-Custom-Header"));
    assertEquals("other", fake.lastRequest.headers().get("X-Other"));
  }

  @Test
  @DisplayName("call() deserializes JSON response to the requested type")
  void call_deserializesResponse() {
    var fake = new FakeHttpClient(json("{\"name\":\"hello\"}"));
    var client = new ApiClientImpl(fake, "https://api.example.com/v1");

    Bean result = client.call("GET", ApiPath.from("/x"), Bean.class);

    assertNotNull(result);
    assertEquals("hello", result.getName());
  }

  @Test
  @DisplayName("call() returns raw body string when responseType is String")
  void call_stringResponseType_returnsRawBody() {
    String body = "{\"raw\":true}";
    var fake = new FakeHttpClient(json(body));
    var client = new ApiClientImpl(fake, "https://api.example.com/v1");

    String result = client.call("GET", ApiPath.from("/x"), String.class);

    assertEquals(body, result);
  }

  @Test
  @DisplayName("call() respects JSON_FAIL_ON_UNKNOWN=true — throws on unknown properties")
  void call_jsonFailOnUnknown_true_throwsOnUnknownProperties() {
    var fake = new FakeHttpClient(json("{\"name\":\"x\",\"unknown\":\"y\"}"));
    ApiClientConfig config =
        ApiClientConfig.builder().jsonFailOnUnknownProperties(true).build();
    var client = new ApiClientImpl(fake, "https://api.example.com/v1", config);

    assertThrows(RuntimeException.class, () -> client.call("GET", ApiPath.from("/x"), Bean.class));
  }

  @Test
  @DisplayName("call() tolerates unknown properties when JSON_FAIL_ON_UNKNOWN=false (default)")
  void call_jsonFailOnUnknown_false_ignoresUnknownProperties() {
    var fake = new FakeHttpClient(json("{\"name\":\"x\",\"unknown\":\"y\"}"));
    var client = new ApiClientImpl(fake, "https://api.example.com/v1");

    Bean result = client.call("GET", ApiPath.from("/x"), Bean.class);

    assertEquals("x", result.getName());
  }

  @Test
  @DisplayName("call() wraps IOException from the HTTP client in UncheckedIOException")
  void call_ioException_wrappedAsUnchecked() {
    var fake = new FakeHttpClient();
    fake.toThrow = new IOException("network failure");
    var client = new ApiClientImpl(fake, "https://api.example.com/v1");

    UncheckedIOException ex =
        assertThrows(
            UncheckedIOException.class,
            () -> client.call("GET", ApiPath.from("/pages"), String.class));

    assertTrue(ex.getMessage().contains("https://api.example.com/v1/pages"));
    assertSame(fake.toThrow, ex.getCause());
  }
}

