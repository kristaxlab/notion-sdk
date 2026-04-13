package io.kristaxlab.notion.model.file;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class FileUploadModeTest {

  @Test
  void singlePartValue() {
    assertEquals("single_part", FileUploadMode.SINGLE_PART.getValue());
  }

  @Test
  void multiPartValue() {
    assertEquals("multi_part", FileUploadMode.MULTI_PART.getValue());
  }

  @Test
  void externalUrlValue() {
    assertEquals("external_url", FileUploadMode.EXTERNAL_URL.getValue());
  }

  @ParameterizedTest
  @EnumSource(FileUploadMode.class)
  void toStringMatchesGetValue(FileUploadMode mode) {
    assertEquals(mode.getValue(), mode.toString());
  }

  @Test
  void enumValuesContainsAllModes() {
    FileUploadMode[] values = FileUploadMode.values();
    assertEquals(3, values.length);
  }

  @Test
  void valueOfReturnsCorrectConstants() {
    assertEquals(FileUploadMode.SINGLE_PART, FileUploadMode.valueOf("SINGLE_PART"));
    assertEquals(FileUploadMode.MULTI_PART, FileUploadMode.valueOf("MULTI_PART"));
    assertEquals(FileUploadMode.EXTERNAL_URL, FileUploadMode.valueOf("EXTERNAL_URL"));
  }
}
