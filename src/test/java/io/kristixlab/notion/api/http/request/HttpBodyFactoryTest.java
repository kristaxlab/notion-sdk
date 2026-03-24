package io.kristixlab.notion.api.http.request;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.request.HttpBodyFactory;
import io.kristixlab.notion.api.http.client.HttpClient.*;
import io.kristixlab.notion.api.http.request.MultipartFormDataRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import org.junit.jupiter.api.*;

/**
 * Unit tests for {@link HttpBodyFactory}.
 *
 * <p>Verifies JSON body creation, multipart body creation, null-body handling, and the {@link
 * HttpBodyFactory#requiresJsonContentType(Object)} helper.
 *
 * <p>All assertions are against the library-agnostic {@link Body} sealed hierarchy — no OkHttp
 * types are used.
 */
class HttpBodyFactoryTest {

  // ---------------------------------------------------------------
  // create() — null body
  // ---------------------------------------------------------------

  @Test
  @DisplayName("create() returns null when body is null")
  void createReturnsNullForNullBody() {
    Body result = HttpBodyFactory.create(null);
    assertNull(result);
  }

  // ---------------------------------------------------------------
  // create() — JSON body
  // ---------------------------------------------------------------

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

  // ---------------------------------------------------------------
  // create() — multipart body (text part)
  // ---------------------------------------------------------------

  @Test
  @DisplayName("create() produces MultipartBody for MultipartFormDataRequest")
  void createMultipartBody() {
    MultipartFormDataRequest multipart = new MultipartFormDataRequest();
    multipart.addPart("key", "val");

    Body result = HttpBodyFactory.create(multipart);

    assertNotNull(result);
    assertInstanceOf(MultipartBody.class, result);

    MultipartBody mb = (MultipartBody) result;
    assertEquals(1, mb.parts().size());
    assertInstanceOf(TextPart.class, mb.parts().get(0));

    TextPart tp = (TextPart) mb.parts().get(0);
    assertEquals("key", tp.name());
    assertEquals("val", tp.value());
  }

  // ---------------------------------------------------------------
  // create() — multipart with file (always InputStreamPart)
  // ---------------------------------------------------------------

  @Test
  @DisplayName("create() always wraps file as InputStreamPart regardless of size")
  void createMultipartWithFileAlwaysStreams() throws IOException {
    File tempFile = Files.createTempFile("hbf-file-", ".bin").toFile();
    tempFile.deleteOnExit();
    Files.write(tempFile.toPath(), new byte[512]); // small file — still streamed

    MultipartFormDataRequest multipart = new MultipartFormDataRequest();
    multipart.addFilePart("file", tempFile, "small.bin", "application/octet-stream");

    Body result = HttpBodyFactory.create(multipart);

    assertNotNull(result);
    assertInstanceOf(MultipartBody.class, result);

    MultipartBody mb = (MultipartBody) result;
    assertEquals(1, mb.parts().size());
    assertInstanceOf(InputStreamPart.class, mb.parts().get(0));

    InputStreamPart isp = (InputStreamPart) mb.parts().get(0);
    assertEquals("file", isp.name());
    assertEquals("small.bin", isp.filename());
    assertEquals("application/octet-stream", isp.contentType());
    assertNotNull(isp.inputStream());
    isp.inputStream().close();
  }

  // ---------------------------------------------------------------
  // create() — multipart with InputStream part
  // ---------------------------------------------------------------

  @Test
  @DisplayName("create() converts InputStreamPart correctly")
  void createMultipartWithInputStreamPart() {
    InputStream is = new ByteArrayInputStream(new byte[] {1, 2, 3});
    MultipartFormDataRequest multipart = new MultipartFormDataRequest();
    multipart.addInputStreamPart("stream", is, "data.bin", "application/octet-stream");

    Body result = HttpBodyFactory.create(multipart);

    assertNotNull(result);
    assertInstanceOf(MultipartBody.class, result);

    MultipartBody mb = (MultipartBody) result;
    assertEquals(1, mb.parts().size());
    assertInstanceOf(InputStreamPart.class, mb.parts().get(0));

    InputStreamPart isp = (InputStreamPart) mb.parts().get(0);
    assertEquals("stream", isp.name());
    assertEquals("data.bin", isp.filename());
    assertSame(is, isp.inputStream());
  }

  // ---------------------------------------------------------------
  // create() — multipart with byte array part
  // ---------------------------------------------------------------

  @Test
  @DisplayName("create() converts ByteArrayPart correctly")
  void createMultipartWithByteArrayPart() {
    MultipartFormDataRequest multipart = new MultipartFormDataRequest();
    multipart.addByteArrayPart("bytes", new byte[] {10, 20, 30}, "payload.bin", "image/png");

    Body result = HttpBodyFactory.create(multipart);

    assertNotNull(result);
    assertInstanceOf(MultipartBody.class, result);

    MultipartBody mb = (MultipartBody) result;
    assertEquals(1, mb.parts().size());
    assertInstanceOf(BytesPart.class, mb.parts().get(0));

    BytesPart bp = (BytesPart) mb.parts().get(0);
    assertEquals("bytes", bp.name());
    assertEquals("payload.bin", bp.filename());
    assertEquals("image/png", bp.contentType());
    assertArrayEquals(new byte[] {10, 20, 30}, bp.bytes());
  }

  // ---------------------------------------------------------------
  // create() — multipart with empty byte array throws
  // ---------------------------------------------------------------

  @Test
  @DisplayName("create() throws for empty byte array part")
  void createMultipartWithEmptyByteArrayThrows() {
    MultipartFormDataRequest multipart = new MultipartFormDataRequest();
    multipart.addByteArrayPart("empty", new byte[0], "empty.bin", "application/octet-stream");

    assertThrows(IllegalArgumentException.class, () -> HttpBodyFactory.create(multipart));
  }

  // ---------------------------------------------------------------
  // requiresJsonContentType()
  // ---------------------------------------------------------------

  @Test
  @DisplayName("requiresJsonContentType returns true for plain objects")
  void requiresJsonContentTypeTrueForPlainObject() {
    assertTrue(HttpBodyFactory.requiresJsonContentType(new SimplePayload("x")));
    assertTrue(HttpBodyFactory.requiresJsonContentType("string body"));
  }

  @Test
  @DisplayName("requiresJsonContentType returns false for MultipartFormDataRequest")
  void requiresJsonContentTypeFalseForMultipart() {
    assertFalse(HttpBodyFactory.requiresJsonContentType(new MultipartFormDataRequest()));
  }

  @Test
  @DisplayName("requiresJsonContentType returns false for null")
  void requiresJsonContentTypeFalseForNull() {
    assertFalse(HttpBodyFactory.requiresJsonContentType(null));
  }

  // ---------------------------------------------------------------
  // Helpers
  // ---------------------------------------------------------------

  /** Minimal POJO for JSON serialization tests. */
  @lombok.Data
  @lombok.AllArgsConstructor
  static class SimplePayload {
    private String value;
  }
}
