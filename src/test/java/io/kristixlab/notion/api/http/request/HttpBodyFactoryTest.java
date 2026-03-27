package io.kristixlab.notion.api.http.request;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.client.HttpClient.*;
import java.io.ByteArrayInputStream;
import java.util.List;
import org.junit.jupiter.api.*;

/**
 * Unit tests for {@link HttpBodyFactory}.
 *
 * <p>Verifies JSON body creation, {@link Body} pass-through, null-body handling, and the {@link
 * HttpBodyFactory#requiresJsonContentType(Object)} helper.
 *
 * <p>All assertions are against the library-agnostic {@link Body} sealed hierarchy — no OkHttp
 * types are used.
 */
class HttpBodyFactoryTest {

  @Test
  @DisplayName("create() returns null when body is null")
  void createReturnsNullForNullBody() {
    Body result = HttpBodyFactory.create(null);
    assertNull(result);
  }

  @Test
  @DisplayName("create() produces StringBody with application/json for a plain object")
  void createJsonBody() {
    SimplePayload payload = new SimplePayload("hello");
    Body result = HttpBodyFactory.create(payload);

    assertNotNull(result);
    assertInstanceOf(StringBody.class, result);

    StringBody sb = (StringBody) result;
    assertEquals("application/json", sb.contentType());
    assertTrue(sb.content().contains("\"value\""), "JSON body should contain the field name");
    assertTrue(sb.content().contains("hello"), "JSON body should contain the field value");
  }

  @Test
  @DisplayName("create() returns a MultipartBody instance unchanged")
  void create_multipartBody_returnedAsIs() {
    MultipartBody multipart = new MultipartBody(List.of(new TextPart("key", "val")));

    Body result = HttpBodyFactory.create(multipart);

    assertSame(multipart, result);
  }

  @Test
  @DisplayName("create() returns a StringBody instance unchanged")
  void create_stringBody_returnedAsIs() {
    StringBody stringBody = new StringBody("{}", "application/json");

    Body result = HttpBodyFactory.create(stringBody);

    assertSame(stringBody, result);
  }

  @Test
  @DisplayName("create() returns an InputStreamBody instance unchanged")
  void create_inputStreamBody_returnedAsIs() {
    InputStreamBody isBody =
        new InputStreamBody(new ByteArrayInputStream(new byte[] {1, 2, 3}), 3, "image/png");

    Body result = HttpBodyFactory.create(isBody);

    assertSame(isBody, result);
  }

  @Test
  @DisplayName("requiresJsonContentType returns true for plain objects")
  void requiresJsonContentTypeTrueForPlainObject() {
    assertTrue(HttpBodyFactory.requiresJsonContentType(new SimplePayload("x")));
    assertTrue(HttpBodyFactory.requiresJsonContentType("string body"));
  }

  @Test
  @DisplayName("requiresJsonContentType returns false for any Body instance")
  void requiresJsonContentTypeFalseForBodyInstance() {
    assertFalse(
        HttpBodyFactory.requiresJsonContentType(
            new MultipartBody(List.of(new TextPart("k", "v")))));
    assertFalse(HttpBodyFactory.requiresJsonContentType(new EmptyBody()));
    assertFalse(HttpBodyFactory.requiresJsonContentType(new StringBody("{}", "application/json")));
  }

  @Test
  @DisplayName("requiresJsonContentType returns false for null")
  void requiresJsonContentTypeFalseForNull() {
    assertFalse(HttpBodyFactory.requiresJsonContentType(null));
  }

  /** Minimal POJO for JSON serialization tests. */
  @lombok.Data
  @lombok.AllArgsConstructor
  static class SimplePayload {
    private String value;
  }
}
