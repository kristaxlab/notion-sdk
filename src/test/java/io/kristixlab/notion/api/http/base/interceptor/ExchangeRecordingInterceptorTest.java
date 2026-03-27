package io.kristixlab.notion.api.http.base.interceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristixlab.notion.api.http.base.client.HttpClient.HttpMethod;
import io.kristixlab.notion.api.http.base.client.HttpClient.HttpRequest;
import io.kristixlab.notion.api.http.base.client.HttpClient.HttpResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import util.TestSerializer;

/** Unit tests for {@link ExchangeRecordingInterceptor#serviceNameFrom}. */
class ExchangeRecordingInterceptorTest {

  private static final String BASE = "https://api.notion.com/v1";
  private static final String UUID = "abc12345-1234-1234-1234-abcdef012345";

  @Test
  void get_collectionEndpoint() {
    assertEquals("pages_retrieve", name(BASE + "/pages", "GET"));
  }

  @Test
  void get_resourceById() {
    assertEquals("pages_retrieve", name(BASE + "/pages/" + UUID, "GET"));
  }

  @Test
  void get_nestedCollection() {
    assertEquals("blocks_children_retrieve", name(BASE + "/blocks/" + UUID + "/children", "GET"));
  }

  @Test
  void get_users_me() {
    assertEquals("users_me_retrieve", name(BASE + "/users/me", "GET"));
  }

  @Test
  void post_create() {
    assertEquals("pages_create", name(BASE + "/pages", "POST"));
  }

  @Test
  void post_queryWithId() {
    assertEquals("databases_query_create", name(BASE + "/databases/" + UUID + "/query", "POST"));
  }

  @Test
  void patch_update() {
    assertEquals("pages_update", name(BASE + "/pages/" + UUID, "PATCH"));
  }

  @Test
  void delete_resource() {
    assertEquals("blocks_delete", name(BASE + "/blocks/" + UUID, "DELETE"));
  }

  @Test
  void get_compactUuidStripped() {
    assertEquals("pages_retrieve", name(BASE + "/pages/abc123def456abc123def456abc123de", "GET"));
  }

  @Test
  void unknownMethod_noSuffix() {
    assertEquals("pages", name(BASE + "/pages", "PUT"));
  }

  @Test
  void malformedUrl_returnsUnknown() {
    assertEquals("unknown", name("not a url %%", "GET"));
  }

  private static String name(String url, String method) {
    return ExchangeRecordingInterceptor.serviceNameFrom(url, method);
  }

  @Test
  void writesRequestAndResponseFiles_andRedactsAuthorization(@TempDir Path tempDir)
      throws IOException {
    ExchangeRecordingInterceptor interceptor =
        new ExchangeRecordingInterceptor(tempDir, new TestSerializer());
    HttpRequest request =
        HttpRequest.builder()
            .url(BASE + "/pages")
            .method(HttpMethod.POST)
            .headers(Map.of("Authorization", "Bearer secret-token", "X-Test", "yes"))
            .build();
    HttpResponse response =
        new HttpResponse(200, Map.of("X-Server", List.of("ok")), "{\"ok\":true}".getBytes());

    interceptor.beforeSend(request);
    interceptor.afterReceive(request, response);

    List<Path> files = Files.list(tempDir).toList();
    assertEquals(2, files.size());

    Path requestFile =
        files.stream()
            .filter(p -> p.getFileName().toString().endsWith("_rq.json"))
            .findFirst()
            .orElse(null);
    Path responseFile =
        files.stream()
            .filter(p -> p.getFileName().toString().endsWith("_rs.json"))
            .findFirst()
            .orElse(null);
    assertNotNull(requestFile);
    assertNotNull(responseFile);

    String requestJson = Files.readString(requestFile);
    String responseJson = Files.readString(responseFile);

    assertTrue(requestJson.contains("\"request_headers\""));
    assertTrue(requestJson.contains("\"Authorization\""));
    assertTrue(requestJson.contains("\"[redacted]\""));
    assertFalse(requestJson.contains("secret-token"));
    assertTrue(responseJson.contains("\"status_code\""));
    assertTrue(responseJson.contains("200"));
    assertTrue(responseJson.contains("\"response_body\""));
  }
}
