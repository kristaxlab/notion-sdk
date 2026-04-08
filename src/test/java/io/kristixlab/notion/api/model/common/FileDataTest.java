package io.kristixlab.notion.api.model.common;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.List;
import org.junit.jupiter.api.Test;

class FileDataTest {

  @Test
  void external_setsTypeAndUrl() {
    FileData data = FileData.external("https://example.com/file.png");

    assertEquals("external", data.getType());
    assertNotNull(data.getExternal());
    assertEquals("https://example.com/file.png", data.getExternal().getUrl());
    assertNull(data.getFile());
    assertNull(data.getFileUpload());
  }

  @Test
  void file_setsTypeAndFile() {
    File file = new File();
    file.setUrl("https://s3.notion.com/file.pdf");

    FileData data = FileData.file(file);

    assertEquals("file", data.getType());
    assertSame(file, data.getFile());
    assertNull(data.getExternal());
    assertNull(data.getFileUpload());
  }

  @Test
  void fileUpload_setsTypeAndId() {
    FileData data = FileData.fileUpload("upload-123");

    assertEquals("file_upload", data.getType());
    assertNotNull(data.getFileUpload());
    assertEquals("upload-123", data.getFileUpload().getId());
    assertNull(data.getExternal());
    assertNull(data.getFile());
  }

  // Builder

  @Test
  void builder_external() {
    FileData data =
        FileData.builder().type("external").external("https://example.com/img.png").build();

    assertEquals("external", data.getType());
    assertNotNull(data.getExternal());
    assertEquals("https://example.com/img.png", data.getExternal().getUrl());
  }

  @Test
  void builder_fileUpload() {
    FileData data = FileData.builder().type("file_upload").fileUpload("upload-abc").build();

    assertEquals("file_upload", data.getType());
    assertNotNull(data.getFileUpload());
    assertEquals("upload-abc", data.getFileUpload().getId());
  }

  @Test
  void builder_withCaption() {
    List<RichText> caption = RichText.of("My caption");

    FileData data =
        FileData.builder()
            .type("external")
            .external("https://example.com/img.png")
            .caption(caption)
            .build();

    assertSame(caption, data.getCaption());
  }

  @Test
  void builder_withName() {
    FileData data =
        FileData.builder()
            .type("external")
            .external("https://example.com/img.png")
            .name("photo.png")
            .build();

    assertEquals("photo.png", data.getName());
  }

  @Test
  void builder_withFile() {
    File file = new File();
    file.setUrl("https://s3.notion.com/test.pdf");

    FileData data = FileData.builder().type("file").file(file).build();

    assertEquals("file", data.getType());
    assertSame(file, data.getFile());
  }

  // Getter/Setter

  @Test
  void noArgConstructor_allFieldsNull() {
    FileData data = new FileData();

    assertNull(data.getType());
    assertNull(data.getExternal());
    assertNull(data.getFile());
    assertNull(data.getFileUpload());
    assertNull(data.getCaption());
    assertNull(data.getName());
  }

  @Test
  void setters_work() {
    FileData data = new FileData();
    data.setType("external");
    data.setName("test.png");

    assertEquals("external", data.getType());
    assertEquals("test.png", data.getName());
  }
}
