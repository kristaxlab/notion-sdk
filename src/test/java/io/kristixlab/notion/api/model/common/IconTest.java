package io.kristixlab.notion.api.model.common;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class IconTest {

  @Test
  void emoji_setsTypeAndEmoji() {
    Icon icon = Icon.emoji("🔥");

    assertEquals("emoji", icon.getType());
    assertEquals("🔥", icon.getEmoji());
    assertNull(icon.getCustomEmoji());
    assertNull(icon.getExternal());
    assertNull(icon.getFile());
  }

  @Test
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
  void external_setsTypeAndUrl() {
    Icon icon = Icon.external("https://example.com/icon.png");

    assertEquals("external", icon.getType());
    assertNotNull(icon.getExternal());
    assertEquals("https://example.com/icon.png", icon.getExternal().getUrl());
    assertNull(icon.getEmoji());
    assertNull(icon.getFile());
  }

  @Test
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
  void noArgConstructor_allFieldsNull() {
    Icon icon = new Icon();

    assertNull(icon.getType());
    assertNull(icon.getEmoji());
    assertNull(icon.getCustomEmoji());
    assertNull(icon.getExternal());
    assertNull(icon.getFile());
  }

  @Test
  void setters_work() {
    Icon icon = new Icon();

    icon.setType("emoji");
    icon.setEmoji("✅");

    assertEquals("emoji", icon.getType());
    assertEquals("✅", icon.getEmoji());
  }
}
