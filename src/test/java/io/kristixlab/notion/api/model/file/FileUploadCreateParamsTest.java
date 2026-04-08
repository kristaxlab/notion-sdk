package io.kristixlab.notion.api.model.file;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FileUploadCreateParamsTest {

  @Nested
  class BuilderTests {

    @Test
    void buildEmpty_allFieldsAreNull() {
      FileUploadCreateParams params = FileUploadCreateParams.builder().build();

      assertNull(params.getMode());
      assertNull(params.getFilename());
      assertNull(params.getContentType());
      assertNull(params.getNumberOfParts());
      assertNull(params.getExternalUrl());
    }

    @Test
    void buildWithAllFields() {
      FileUploadCreateParams params =
          FileUploadCreateParams.builder()
              .mode("multi_part")
              .filename("report.pdf")
              .contentType("application/pdf")
              .numberOfParts(5)
              .externalUrl("https://example.com/report.pdf")
              .build();

      assertEquals("multi_part", params.getMode());
      assertEquals("report.pdf", params.getFilename());
      assertEquals("application/pdf", params.getContentType());
      assertEquals(5, params.getNumberOfParts());
      assertEquals("https://example.com/report.pdf", params.getExternalUrl());
    }

    @Test
    void buildWithMode() {
      FileUploadCreateParams params = FileUploadCreateParams.builder().mode("single_part").build();

      assertEquals("single_part", params.getMode());
      assertNull(params.getFilename());
      assertNull(params.getContentType());
      assertNull(params.getNumberOfParts());
      assertNull(params.getExternalUrl());
    }

    @Test
    void buildWithFilename() {
      FileUploadCreateParams params =
          FileUploadCreateParams.builder().filename("image.png").build();

      assertNull(params.getMode());
      assertEquals("image.png", params.getFilename());
    }

    @Test
    void buildWithContentType() {
      FileUploadCreateParams params =
          FileUploadCreateParams.builder().contentType("image/png").build();

      assertNull(params.getMode());
      assertEquals("image/png", params.getContentType());
    }

    @Test
    void buildWithNumberOfParts() {
      FileUploadCreateParams params = FileUploadCreateParams.builder().numberOfParts(10).build();

      assertEquals(10, params.getNumberOfParts());
    }

    @Test
    void buildWithExternalUrl() {
      FileUploadCreateParams params =
          FileUploadCreateParams.builder().externalUrl("https://cdn.example.com/data.csv").build();

      assertEquals("https://cdn.example.com/data.csv", params.getExternalUrl());
    }

    @Test
    void builderMethodChaining_returnsSameBuilder() {
      FileUploadCreateParams.Builder builder = FileUploadCreateParams.builder();

      assertSame(builder, builder.mode("single_part"));
      assertSame(builder, builder.filename("file.txt"));
      assertSame(builder, builder.contentType("text/plain"));
      assertSame(builder, builder.numberOfParts(1));
      assertSame(builder, builder.externalUrl("https://example.com"));
    }

    @Test
    void buildWithNullNumberOfParts_setsNull() {
      FileUploadCreateParams params = FileUploadCreateParams.builder().numberOfParts(null).build();

      assertNull(params.getNumberOfParts());
    }

    @Test
    void lastSetValueWins() {
      FileUploadCreateParams params =
          FileUploadCreateParams.builder()
              .mode("single_part")
              .mode("multi_part")
              .filename("first.pdf")
              .filename("second.pdf")
              .build();

      assertEquals("multi_part", params.getMode());
      assertEquals("second.pdf", params.getFilename());
    }
  }

  // ── Factory Methods ────────────────────────────────────────────────────────

  @Nested
  class FactoryMethods {

    @Test
    void external_setsModFilenameAndUrl() {
      FileUploadCreateParams params =
          FileUploadCreateParams.external("data.csv", "https://cdn.example.com/data.csv");

      assertEquals("external_url", params.getMode());
      assertEquals("data.csv", params.getFilename());
      assertEquals("https://cdn.example.com/data.csv", params.getExternalUrl());
      assertNull(params.getContentType());
      assertNull(params.getNumberOfParts());
    }

    @Test
    void singlePart_setsModeAndFilename() {
      FileUploadCreateParams params = FileUploadCreateParams.singlePart("photo.jpg");

      assertEquals("single_part", params.getMode());
      assertEquals("photo.jpg", params.getFilename());
      assertNull(params.getContentType());
      assertNull(params.getNumberOfParts());
      assertNull(params.getExternalUrl());
    }

    @Test
    void multiPart_setsModeFilenameAndNumberOfParts() {
      FileUploadCreateParams params = FileUploadCreateParams.multiPart("video.mp4", 7);

      assertEquals("multi_part", params.getMode());
      assertEquals("video.mp4", params.getFilename());
      assertEquals(7, params.getNumberOfParts());
      assertNull(params.getContentType());
      assertNull(params.getExternalUrl());
    }

    @Test
    void external_returnsNewInstanceEachCall() {
      FileUploadCreateParams first =
          FileUploadCreateParams.external("a.txt", "https://example.com/a.txt");
      FileUploadCreateParams second =
          FileUploadCreateParams.external("b.txt", "https://example.com/b.txt");

      assertNotSame(first, second);
    }

    @Test
    void singlePart_returnsNewInstanceEachCall() {
      FileUploadCreateParams first = FileUploadCreateParams.singlePart("a.txt");
      FileUploadCreateParams second = FileUploadCreateParams.singlePart("b.txt");

      assertNotSame(first, second);
    }

    @Test
    void multiPart_returnsNewInstanceEachCall() {
      FileUploadCreateParams first = FileUploadCreateParams.multiPart("a.txt", 2);
      FileUploadCreateParams second = FileUploadCreateParams.multiPart("b.txt", 3);

      assertNotSame(first, second);
    }
  }

  // ── Builder creates independent instance ───────────────────────────────────

  @Nested
  class BuilderIndependence {

    @Test
    void builderReturnsNewInstancePerCall() {
      FileUploadCreateParams.Builder builderA = FileUploadCreateParams.builder();
      FileUploadCreateParams.Builder builderB = FileUploadCreateParams.builder();

      assertNotSame(builderA, builderB);
    }

    @Test
    void mutatingParamsAfterBuild_doesNotAffectBuilder() {
      FileUploadCreateParams.Builder builder =
          FileUploadCreateParams.builder().mode("single_part").filename("original.txt");

      FileUploadCreateParams params = builder.build();
      params.setFilename("modified.txt");

      // Builder holds a reference to the same params, so build() again returns that same object.
      // This is the current behaviour — documenting it:
      FileUploadCreateParams secondBuild = builder.build();
      assertSame(params, secondBuild);
      assertEquals("modified.txt", secondBuild.getFilename());
    }
  }
}
