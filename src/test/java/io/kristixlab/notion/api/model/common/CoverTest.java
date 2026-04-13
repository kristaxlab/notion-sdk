package io.kristixlab.notion.api.model.common;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Cover behaviors")
class CoverTest {

  @Test
  @DisplayName("works for external cover URL")
  void external_setsTypeAndUrl() {
    Cover cover = Cover.external("https://example.com/cover.png");

    assertEquals("external", cover.getType());
    assertNotNull(cover.getExternal());
    assertEquals("https://example.com/cover.png", cover.getExternal().getUrl());
    assertNull(cover.getFileUpload());
    assertNull(cover.getFile());
  }

  @Test
  @DisplayName("works for file upload cover id")
  void fileUpload_setsTypeAndId() {
    Cover cover = Cover.fileUpload("file-upload-id-123");

    assertEquals("file_upload", cover.getType());
    assertNotNull(cover.getFileUpload());
    assertEquals("file-upload-id-123", cover.getFileUpload().getId());
    assertNull(cover.getExternal());
    assertNull(cover.getFile());
  }

  @Test
  @DisplayName("works for null external URL")
  void external_withNullUrl_keepsExternalObjectWithNullUrl() {
    Cover cover = Cover.external(null);

    assertEquals("external", cover.getType());
    assertNotNull(cover.getExternal());
    assertNull(cover.getExternal().getUrl());
    assertNull(cover.getFileUpload());
  }

  @Test
  @DisplayName("works for null file upload id")
  void fileUpload_withNullId_keepsFileUploadObjectWithNullId() {
    Cover cover = Cover.fileUpload(null);

    assertEquals("file_upload", cover.getType());
    assertNotNull(cover.getFileUpload());
    assertNull(cover.getFileUpload().getId());
    assertNull(cover.getExternal());
  }

  @Test
  @DisplayName("new cover has all fields unset")
  void noArgConstructor_allFieldsNull() {
    Cover cover = new Cover();

    assertNull(cover.getType());
    assertNull(cover.getExternal());
    assertNull(cover.getFileUpload());
    assertNull(cover.getFile());
  }
}
