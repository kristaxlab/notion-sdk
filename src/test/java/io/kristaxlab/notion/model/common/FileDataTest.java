package io.kristaxlab.notion.model.common;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.fluent.NotionText;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileDataTest {

  // Builder

  @Test
  @DisplayName("builder external")
  void builder_external() {
    FileData data = FileData.builder().externalUrl("https://example.com/img.png").build();

    assertEquals("external", data.getType());
    assertNotNull(data.getExternal());
    assertEquals("https://example.com/img.png", data.getExternal().getUrl());
  }

  @Test
  @DisplayName("builder file upload")
  void builder_fileUpload() {
    FileData data = FileData.builder().fileUpload("upload-abc").build();

    assertEquals("file_upload", data.getType());
    assertNotNull(data.getFileUpload());
    assertEquals("upload-abc", data.getFileUpload().getId());
  }

  @Test
  @DisplayName("builder with caption")
  void builder_withCaption() {
    FileData data =
        FileData.builder()
            .externalUrl("https://example.com/img.png")
            .caption(NotionText.plainText("My caption 1"))
            .caption(NotionText.plainText("My caption 2"))
            .build();

    assertEquals(2, data.getCaption().size());
  }
}
