package io.kristixlab.notion.api.http.base.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristixlab.notion.api.http.base.client.HttpClient.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
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

  @Test
  @DisplayName("send() supports PUT with EmptyBody")
  void send_putWithEmptyBody() throws Exception {
    try (MockWebServer server = new MockWebServer()) {
      server.enqueue(new MockResponse().setResponseCode(204));
      server.start();
      OkHttp3Client client = new OkHttp3Client(new OkHttpClient());
      HttpRequest request = HttpRequest.builder()
          .url(server.url("/put").toString())
          .method(HttpMethod.PUT)
          .body(new EmptyBody())
          .build();
      HttpResponse response = client.send(request);
      RecordedRequest recorded = server.takeRequest();
      assertEquals(204, response.statusCode());
      assertEquals("PUT", recorded.getMethod());
      assertEquals(0, recorded.getBodySize());
    }
  }

  @Test
  @DisplayName("send() supports PATCH with BytesBody")
  void send_patchWithBytesBody() throws Exception {
    try (MockWebServer server = new MockWebServer()) {
      server.enqueue(new MockResponse().setResponseCode(200));
      server.start();
      OkHttp3Client client = new OkHttp3Client(new OkHttpClient());
      byte[] bytes = "patch-bytes".getBytes(StandardCharsets.UTF_8);
      HttpRequest request = HttpRequest.builder()
          .url(server.url("/patch").toString())
          .method(HttpMethod.PATCH)
          .body(new BytesBody(bytes, "application/octet-stream"))
          .build();
      client.send(request);
      RecordedRequest recorded = server.takeRequest();
      assertEquals("PATCH", recorded.getMethod());
      assertEquals("patch-bytes", recorded.getBody().readUtf8());
      assertEquals("application/octet-stream", recorded.getHeader("Content-Type"));
    }
  }

  @Test
  @DisplayName("send() supports DELETE with no body")
  void send_deleteNoBody() throws Exception {
    try (MockWebServer server = new MockWebServer()) {
      server.enqueue(new MockResponse().setResponseCode(204));
      server.start();
      OkHttp3Client client = new OkHttp3Client(new OkHttpClient());
      HttpRequest request = HttpRequest.builder()
          .url(server.url("/delete").toString())
          .method(HttpMethod.DELETE)
          .build();
      client.send(request);
      RecordedRequest recorded = server.takeRequest();
      assertEquals("DELETE", recorded.getMethod());
      assertEquals(0, recorded.getBodySize());
    }
  }

  @Test
  @DisplayName("send() supports FileBody")
  void send_postFileBody() throws Exception {
    try (MockWebServer server = new MockWebServer()) {
      server.enqueue(new MockResponse().setResponseCode(200));
      server.start();
      File temp = File.createTempFile("okhttp3clienttest", ".txt");
      temp.deleteOnExit();
      String content = "file-body-content";
      try (FileOutputStream fos = new FileOutputStream(temp)) {
        fos.write(content.getBytes(StandardCharsets.UTF_8));
      }
      OkHttp3Client client = new OkHttp3Client(new OkHttpClient());
      HttpRequest request = HttpRequest.builder()
          .url(server.url("/file").toString())
          .method(HttpMethod.POST)
          .body(new FileBody(temp, "text/plain"))
          .build();
      client.send(request);
      RecordedRequest recorded = server.takeRequest();
      assertEquals("POST", recorded.getMethod());
      assertEquals(content, recorded.getBody().readUtf8());
      assertEquals("text/plain", recorded.getHeader("Content-Type"));
    }
  }

  @Test
  @DisplayName("send() supports InputStreamBody")
  void send_postInputStreamBody() throws Exception {
    try (MockWebServer server = new MockWebServer()) {
      server.enqueue(new MockResponse().setResponseCode(200));
      server.start();
      String content = "input-stream-body";
      ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
      OkHttp3Client client = new OkHttp3Client(new OkHttpClient());
      HttpRequest request = HttpRequest.builder()
          .url(server.url("/stream").toString())
          .method(HttpMethod.POST)
          .body(new InputStreamBody(in, content.length(), "text/plain"))
          .build();
      client.send(request);
      RecordedRequest recorded = server.takeRequest();
      assertEquals("POST", recorded.getMethod());
      assertEquals(content, recorded.getBody().readUtf8());
      assertEquals("text/plain", recorded.getHeader("Content-Type"));
    }
  }

  @Test
  @DisplayName("send() supports MultipartBody with all part types")
  void send_postMultipartBody_allParts() throws Exception {
    try (MockWebServer server = new MockWebServer()) {
      server.enqueue(new MockResponse().setResponseCode(200));
      server.start();
      // File part
      File temp = File.createTempFile("okhttp3clienttest-mp", ".txt");
      temp.deleteOnExit();
      String fileContent = "file-part-content";
      try (FileOutputStream fos = new FileOutputStream(temp)) {
        fos.write(fileContent.getBytes(StandardCharsets.UTF_8));
      }
      // Bytes part
      byte[] bytes = "bytes-part-content".getBytes(StandardCharsets.UTF_8);
      // InputStream part
      String isContent = "is-part-content";
      ByteArrayInputStream is = new ByteArrayInputStream(isContent.getBytes(StandardCharsets.UTF_8));
      MultipartBody multipart = new MultipartBody(List.of(
          new TextPart("field1", "value1"),
          new FilePart("file1", "file.txt", temp, "text/plain"),
          new BytesPart("bytes1", "bytes.bin", bytes, "application/octet-stream"),
          new InputStreamPart("is1", "is.txt", is, "text/plain")
      ));
      OkHttp3Client client = new OkHttp3Client(new OkHttpClient());
      HttpRequest request = HttpRequest.builder()
          .url(server.url("/multipart").toString())
          .method(HttpMethod.POST)
          .body(multipart)
          .build();
      client.send(request);
      RecordedRequest recorded = server.takeRequest();
      String body = recorded.getBody().readUtf8();
      assertEquals("POST", recorded.getMethod());
      assertTrue(body.contains("form-data; name=\"field1\""));
      assertTrue(body.contains("value1"));
      assertTrue(body.contains("form-data; name=\"file1\"; filename=\"file.txt\""));
      assertTrue(body.contains(fileContent));
      assertTrue(body.contains("form-data; name=\"bytes1\"; filename=\"bytes.bin\""));
      assertTrue(body.contains("bytes-part-content"));
      assertTrue(body.contains("form-data; name=\"is1\"; filename=\"is.txt\""));
      assertTrue(body.contains(isContent));
      String contentType = recorded.getHeader("Content-Type");
      assertTrue(contentType != null && contentType.startsWith("multipart/form-data;"));
    }
  }

  @Test
  @DisplayName("send() throws if contentType is missing or blank for StringBody")
  void send_stringBodyMissingContentType_throws() {
    OkHttp3Client client = new OkHttp3Client(new OkHttpClient());
    HttpRequest request = HttpRequest.builder()
        .url("http://localhost/ct")
        .method(HttpMethod.POST)
        .body(new StringBody("abc", " "))
        .build();
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> client.send(request));
    assertTrue(ex.getMessage().contains("contentType is required"));
  }

  @Test
  @DisplayName("send() throws if contentType is invalid for StringBody")
  void send_stringBodyInvalidContentType_throws() {
    OkHttp3Client client = new OkHttp3Client(new OkHttpClient());
    HttpRequest request = HttpRequest.builder()
        .url("http://localhost/ct")
        .method(HttpMethod.POST)
        .body(new StringBody("abc", "invalid/type;\u0000"))
        .build();
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> client.send(request));
    assertTrue(ex.getMessage().contains("Invalid contentType"));
  }

  @Test
  @DisplayName("send() throws if request is null")
  void send_nullRequest_throws() {
    OkHttp3Client client = new OkHttp3Client(new OkHttpClient());
    assertThrows(NullPointerException.class, () -> client.send(null));
  }

  @Test
  @DisplayName("HttpRequest.Builder throws if url or method is null")
  void httpRequestBuilder_nullUrlOrMethod_throws() {
    // url null
    HttpRequest.Builder builder = HttpRequest.builder().method(HttpMethod.POST);
    assertThrows(IllegalArgumentException.class, builder::build);
    // method null
    builder = HttpRequest.builder().url("http://localhost").method(null);
    assertThrows(IllegalArgumentException.class, builder::build);
  }
}
