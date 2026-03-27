package io.kristixlab.notion.api.http.base.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristixlab.notion.api.http.base.client.HttpClient.HttpMethod;
import io.kristixlab.notion.api.http.base.client.HttpClient.HttpRequest;
import io.kristixlab.notion.api.http.base.client.HttpClient.HttpResponse;
import io.kristixlab.notion.api.http.base.client.HttpClient.StringBody;
import java.io.IOException;
import java.util.Map;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link OkHttp3Client}. */
class OkHttp3ClientTest {

  @Test
  @DisplayName("send() forwards method, URL and headers and maps response")
  void send_forwardsRequestAndMapsResponse() throws Exception {
    try (MockWebServer server = new MockWebServer()) {
      server.enqueue(
          new MockResponse().setResponseCode(200).setHeader("X-Server", "ok").setBody("pong"));
      server.start();

      OkHttp3Client client = new OkHttp3Client(new OkHttpClient());
      String url = server.url("/ping?x=1").toString();

      HttpRequest request =
          HttpRequest.builder()
              .url(url)
              .method(HttpMethod.GET)
              .headers(Map.of("Accept", "application/json"))
              .build();

      HttpResponse response = client.send(request);
      RecordedRequest recorded = server.takeRequest();

      assertEquals(200, response.statusCode());
      assertEquals("pong", response.bodyAsString());
      assertEquals("ok", response.headers().get("X-Server").get(0));
      assertEquals("GET", recorded.getMethod());
      assertEquals("/ping?x=1", recorded.getPath());
      assertEquals("application/json", recorded.getHeader("Accept"));
    }
  }

  @Test
  @DisplayName("send() sends StringBody with content type")
  void send_postStringBody_withContentType() throws Exception {
    try (MockWebServer server = new MockWebServer()) {
      server.enqueue(new MockResponse().setResponseCode(201).setBody("{\"ok\":true}"));
      server.start();

      OkHttp3Client client = new OkHttp3Client(new OkHttpClient());
      String json = "{\"title\":\"hello\"}";
      HttpRequest request =
          HttpRequest.builder()
              .url(server.url("/pages").toString())
              .method(HttpMethod.POST)
              .body(new StringBody(json, "application/json"))
              .build();

      HttpResponse response = client.send(request);
      RecordedRequest recorded = server.takeRequest();

      assertEquals(201, response.statusCode());
      assertEquals(json, recorded.getBody().readUtf8());
      assertEquals("application/json; charset=utf-8", recorded.getHeader("Content-Type"));
    }
  }

  @Test
  @DisplayName("send() ignores request body for GET")
  void send_getWithBody_ignoresBody() throws IOException, InterruptedException {
    try (MockWebServer server = new MockWebServer()) {
      server.enqueue(new MockResponse().setResponseCode(200).setBody("ok"));
      server.start();

      OkHttp3Client client = new OkHttp3Client(new OkHttpClient());
      HttpRequest request =
          HttpRequest.builder()
              .url(server.url("/pages").toString())
              .method(HttpMethod.GET)
              .body(new StringBody("{\"ignored\":true}", "application/json"))
              .build();

      client.send(request);
      RecordedRequest recorded = server.takeRequest();

      assertEquals("GET", recorded.getMethod());
      assertEquals(0, recorded.getBodySize());
      assertTrue(recorded.getBody().readUtf8().isEmpty());
      assertNull(recorded.getHeader("Content-Type"));
    }
  }
}
