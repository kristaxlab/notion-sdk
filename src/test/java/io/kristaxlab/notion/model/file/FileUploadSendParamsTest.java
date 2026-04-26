package io.kristaxlab.notion.model.file;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FileUploadSendParamsTest {

  @Nested
  class BuilderHappyPaths {

    @Test
    @DisplayName("build with input stream")
    void buildWithInputStream() {
      InputStream is = new ByteArrayInputStream(new byte[] {1, 2, 3});

      FileUploadSendParams params =
          FileUploadSendParams.builder()
              .inputStream(is)
              .contentType("application/pdf")
              .filename("report.pdf")
              .partNumber(1)
              .build();

      assertSame(is, params.getInputStream());
      assertNull(params.getBytes());
      assertNull(params.getFile());
      assertEquals("application/pdf", params.getContentType());
      assertEquals("report.pdf", params.getFilename());
      assertEquals(1, params.getPartNumber());
    }

    @Test
    @DisplayName("build with bytes")
    void buildWithBytes() {
      byte[] data = {10, 20, 30};

      FileUploadSendParams params =
          FileUploadSendParams.builder()
              .bytes(data)
              .contentType("image/png")
              .filename("icon.png")
              .build();

      assertSame(data, params.getBytes());
      assertNull(params.getInputStream());
      assertNull(params.getFile());
      assertEquals("image/png", params.getContentType());
      assertEquals("icon.png", params.getFilename());
    }

    @Test
    @DisplayName("build with file")
    void buildWithFile() {
      File file = new File("document.pdf");

      FileUploadSendParams params =
          FileUploadSendParams.builder()
              .file(file)
              .contentType("application/pdf")
              .filename("document.pdf")
              .build();

      assertSame(file, params.getFile());
      assertNull(params.getInputStream());
      assertNull(params.getBytes());
      assertEquals("application/pdf", params.getContentType());
      assertEquals("document.pdf", params.getFilename());
    }

    @Test
    @DisplayName("build with part number null")
    void buildWithPartNumberNull() {
      FileUploadSendParams params =
          FileUploadSendParams.builder().bytes(new byte[] {1}).partNumber(null).build();

      assertNull(params.getPartNumber());
    }

    @Test
    @DisplayName("builder method chaining returns same builder")
    void builderMethodChaining_returnsSameBuilder() {
      FileUploadSendParams.Builder builder = FileUploadSendParams.builder();

      assertSame(builder, builder.contentType("text/plain"));
      assertSame(builder, builder.filename("readme.txt"));
      assertSame(builder, builder.partNumber(5));
    }

    @Test
    @DisplayName("builder input stream chaining returns same builder")
    void builderInputStreamChaining_returnsSameBuilder() {
      FileUploadSendParams.Builder builder = FileUploadSendParams.builder();
      InputStream is = new ByteArrayInputStream(new byte[] {1});

      assertSame(builder, builder.inputStream(is));
    }

    @Test
    @DisplayName("builder bytes chaining returns same builder")
    void builderBytesChaining_returnsSameBuilder() {
      FileUploadSendParams.Builder builder = FileUploadSendParams.builder();

      assertSame(builder, builder.bytes(new byte[] {1}));
    }

    @Test
    @DisplayName("builder file chaining returns same builder")
    void builderFileChaining_returnsSameBuilder() {
      FileUploadSendParams.Builder builder = FileUploadSendParams.builder();

      assertSame(builder, builder.file(new File("test.txt")));
    }

    @Test
    @DisplayName("build returns new instance each call")
    void buildReturnsNewInstanceEachCall() {
      FileUploadSendParams.Builder builder =
          FileUploadSendParams.builder().bytes(new byte[] {1}).filename("a.txt");

      FileUploadSendParams first = builder.build();
      FileUploadSendParams second = builder.build();

      assertNotSame(first, second);
      assertEquals("a.txt", first.getFilename());
      assertEquals("a.txt", second.getFilename());
    }
  }

  @Nested
  class BuilderMutualExclusion {

    @Test
    @DisplayName("input stream after bytes throws")
    void inputStreamAfterBytes_throws() {
      FileUploadSendParams.Builder builder = FileUploadSendParams.builder().bytes(new byte[] {1});

      IllegalArgumentException ex =
          assertThrows(
              IllegalArgumentException.class,
              () -> builder.inputStream(new ByteArrayInputStream(new byte[] {2})));
      assertTrue(ex.getMessage().contains("bytes"));
    }

    @Test
    @DisplayName("input stream after file throws")
    void inputStreamAfterFile_throws() {
      FileUploadSendParams.Builder builder = FileUploadSendParams.builder().file(new File("f.txt"));

      IllegalArgumentException ex =
          assertThrows(
              IllegalArgumentException.class,
              () -> builder.inputStream(new ByteArrayInputStream(new byte[] {2})));
      assertTrue(ex.getMessage().contains("file"));
    }

    @Test
    @DisplayName("bytes after input stream throws")
    void bytesAfterInputStream_throws() {
      FileUploadSendParams.Builder builder =
          FileUploadSendParams.builder().inputStream(new ByteArrayInputStream(new byte[] {1}));

      IllegalArgumentException ex =
          assertThrows(IllegalArgumentException.class, () -> builder.bytes(new byte[] {2}));
      assertTrue(ex.getMessage().contains("inputStream"));
    }

    @Test
    @DisplayName("bytes after file throws")
    void bytesAfterFile_throws() {
      FileUploadSendParams.Builder builder = FileUploadSendParams.builder().file(new File("f.txt"));

      IllegalArgumentException ex =
          assertThrows(IllegalArgumentException.class, () -> builder.bytes(new byte[] {2}));
      assertTrue(ex.getMessage().contains("file"));
    }

    @Test
    @DisplayName("file after input stream throws")
    void fileAfterInputStream_throws() {
      FileUploadSendParams.Builder builder =
          FileUploadSendParams.builder().inputStream(new ByteArrayInputStream(new byte[] {1}));

      IllegalArgumentException ex =
          assertThrows(IllegalArgumentException.class, () -> builder.file(new File("f.txt")));
      assertTrue(ex.getMessage().contains("inputStream"));
    }

    @Test
    @DisplayName("file after bytes throws")
    void fileAfterBytes_throws() {
      FileUploadSendParams.Builder builder = FileUploadSendParams.builder().bytes(new byte[] {1});

      IllegalArgumentException ex =
          assertThrows(IllegalArgumentException.class, () -> builder.file(new File("f.txt")));
      assertTrue(ex.getMessage().contains("bytes"));
    }
  }

  @Nested
  class BuilderNullValidation {

    @Test
    @DisplayName("input stream null throws")
    void inputStreamNull_throws() {
      IllegalArgumentException ex =
          assertThrows(
              IllegalArgumentException.class,
              () -> FileUploadSendParams.builder().inputStream(null));
      assertTrue(ex.getMessage().contains("InputStream"));
    }

    @Test
    @DisplayName("bytes null throws")
    void bytesNull_throws() {
      IllegalArgumentException ex =
          assertThrows(
              IllegalArgumentException.class, () -> FileUploadSendParams.builder().bytes(null));
      assertTrue(ex.getMessage().contains("Bytes"));
    }

    @Test
    @DisplayName("file null throws")
    void fileNull_throws() {
      IllegalArgumentException ex =
          assertThrows(
              IllegalArgumentException.class, () -> FileUploadSendParams.builder().file(null));
      assertTrue(ex.getMessage().contains("File"));
    }
  }

  @Nested
  class BuilderBuildValidation {

    @Test
    @DisplayName("build with no content source throws")
    void buildWithNoContentSource_throws() {
      FileUploadSendParams.Builder builder = FileUploadSendParams.builder().filename("test.txt");

      IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
      assertTrue(ex.getMessage().contains("inputStream"));
      assertTrue(ex.getMessage().contains("bytes"));
      assertTrue(ex.getMessage().contains("file"));
    }

    @Test
    @DisplayName("build empty throws")
    void buildEmpty_throws() {
      assertThrows(IllegalStateException.class, () -> FileUploadSendParams.builder().build());
    }
  }

  @Nested
  class OfFactoryMethods {

    @Test
    @DisplayName("of input stream sets fields correctly")
    void ofInputStream_setsFieldsCorrectly() {
      InputStream is = new ByteArrayInputStream(new byte[] {4, 5, 6});

      FileUploadSendParams params = FileUploadSendParams.of(is, "text/csv");

      assertSame(is, params.getInputStream());
      assertNull(params.getBytes());
      assertNull(params.getFile());
      assertEquals("text/csv", params.getContentType());
      assertNull(params.getPartNumber());
    }

    @Test
    @DisplayName("of bytes sets fields correctly")
    void ofBytes_setsFieldsCorrectly() {
      byte[] data = {7, 8, 9};

      FileUploadSendParams params = FileUploadSendParams.of(data, "application/octet-stream");

      assertSame(data, params.getBytes());
      assertNull(params.getInputStream());
      assertNull(params.getFile());
      assertEquals("application/octet-stream", params.getContentType());
      assertNull(params.getPartNumber());
    }

    @Test
    @DisplayName("of file sets fields correctly")
    void ofFile_setsFieldsCorrectly() {
      File file = new File("archive.zip");

      FileUploadSendParams params = FileUploadSendParams.of(file, "application/zip");

      assertSame(file, params.getFile());
      assertNull(params.getInputStream());
      assertNull(params.getBytes());
      assertEquals("application/zip", params.getContentType());
      assertNull(params.getPartNumber());
    }

    @Test
    @DisplayName("of input stream returns new instance each call")
    void ofInputStream_returnsNewInstanceEachCall() {
      InputStream is = new ByteArrayInputStream(new byte[] {1});
      FileUploadSendParams a = FileUploadSendParams.of(is, "t");
      FileUploadSendParams b = FileUploadSendParams.of(is, "t");

      assertNotSame(a, b);
    }

    @Test
    @DisplayName("of bytes returns new instance each call")
    void ofBytes_returnsNewInstanceEachCall() {
      byte[] data = {1};
      FileUploadSendParams a = FileUploadSendParams.of(data, "t");
      FileUploadSendParams b = FileUploadSendParams.of(data, "t");

      assertNotSame(a, b);
    }

    @Test
    @DisplayName("of file returns new instance each call")
    void ofFile_returnsNewInstanceEachCall() {
      File file = new File("f.txt");
      FileUploadSendParams a = FileUploadSendParams.of(file, "t");
      FileUploadSendParams b = FileUploadSendParams.of(file, "t");

      assertNotSame(a, b);
    }

    @Test
    @DisplayName("of input stream null throws")
    void ofInputStreamNull_throws() {
      assertThrows(
          IllegalArgumentException.class,
          () -> FileUploadSendParams.of((InputStream) null, "text/plain"));
    }

    @Test
    @DisplayName("of bytes null throws")
    void ofBytesNull_throws() {
      assertThrows(
          IllegalArgumentException.class,
          () -> FileUploadSendParams.of((byte[]) null, "text/plain"));
    }

    @Test
    @DisplayName("of file null throws")
    void ofFileNull_throws() {
      assertThrows(
          IllegalArgumentException.class, () -> FileUploadSendParams.of((File) null, "text/plain"));
    }
  }

  @Nested
  class BuilderIndependence {

    @Test
    @DisplayName("two builders are independent")
    void twoBuilders_areIndependent() {
      FileUploadSendParams.Builder builderA = FileUploadSendParams.builder();
      FileUploadSendParams.Builder builderB = FileUploadSendParams.builder();

      assertNotSame(builderA, builderB);
    }

    @Test
    @DisplayName("setting field on one builder does not affect other")
    void settingFieldOnOneBuilder_doesNotAffectOther() {
      FileUploadSendParams.Builder builderA =
          FileUploadSendParams.builder().bytes(new byte[] {1}).filename("a.txt");
      FileUploadSendParams.Builder builderB =
          FileUploadSendParams.builder().bytes(new byte[] {2}).filename("b.txt");

      FileUploadSendParams paramsA = builderA.build();
      FileUploadSendParams paramsB = builderB.build();

      assertEquals("a.txt", paramsA.getFilename());
      assertNotNull(paramsA.getBytes());
      assertEquals("b.txt", paramsB.getFilename());
      assertNotNull(paramsB.getBytes());
    }
  }
}
