package io.kristixlab.notion.api.model.common;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Icon behaviors")
class IconTest {

  @Test
  @DisplayName("works for emoji icon")
  void emoji_setsTypeAndEmoji() {
    Icon icon = Icon.emoji("🔥");

    assertEquals("emoji", icon.getType());
    assertEquals("🔥", icon.getEmoji());
    assertNull(icon.getCustomEmoji());
    assertNull(icon.getExternal());
    assertNull(icon.getFile());
  }

  @Test
  @DisplayName("works for custom emoji icon")
  void customEmoji_setsTypeIdAndName() {
    Icon icon = Icon.customEmoji("id-123", "fire");

    assertEquals("custom_emoji", icon.getType());
    assertNotNull(icon.getCustomEmoji());
    assertEquals("id-123", icon.getCustomEmoji().getId());
    assertEquals("fire", icon.getCustomEmoji().getName());
    assertNull(icon.getEmoji());
    assertNull(icon.getExternal());
  }

  @Test
  @DisplayName("works for external icon URL")
  void external_setsTypeAndUrl() {
    Icon icon = Icon.external("https://example.com/icon.png");

    assertEquals("external", icon.getType());
    assertNotNull(icon.getExternal());
    assertEquals("https://example.com/icon.png", icon.getExternal().getUrl());
    assertNull(icon.getEmoji());
    assertNull(icon.getFile());
  }

  @Test
  @DisplayName("works for file upload icon id")
  void file_setsTypeAndFile() {
    Icon icon = Icon.fileUpload("file-upload-id-123");

    assertEquals("file_upload", icon.getType());
    assertNotNull(icon.getFileUpload());
    assertEquals("file-upload-id-123", icon.getFileUpload().getId());
    assertNull(icon.getEmoji());
    assertNull(icon.getExternal());
  }

  // getter/setter

  @Test
  @DisplayName("new icon has all fields unset")
  void noArgConstructor_allFieldsNull() {
    Icon icon = new Icon();

    assertNull(icon.getType());
    assertNull(icon.getEmoji());
    assertNull(icon.getCustomEmoji());
    assertNull(icon.getExternal());
    assertNull(icon.getFile());
  }

  @Test
  @DisplayName("setters update icon fields")
  void setters_work() {
    Icon icon = new Icon();

    icon.setType("emoji");
    icon.setEmoji("✅");

    assertEquals("emoji", icon.getType());
    assertEquals("✅", icon.getEmoji());
  }
}
