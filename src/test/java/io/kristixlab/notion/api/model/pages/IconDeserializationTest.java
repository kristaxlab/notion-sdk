package io.kristixlab.notion.api.model.pages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kristixlab.notion.api.model.common.Icon;
import java.io.InputStream;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IconDeserializationTest {

  private static Map<String, Icon> icons;

  @BeforeAll
  static void init() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    InputStream is =
        IconDeserializationTest.class
            .getClassLoader()
            .getResourceAsStream("notion-json-examples/icon-examples.json");
    assertNotNull(is, "Test JSON file not found");
    icons = mapper.readValue(is, new TypeReference<Map<String, Icon>>() {});
    assertNotNull(icons);
  }

  @Test
  void testEmojiIcon() {
    assertNotNull(icons.get("emojiIcon"));
    Icon icon = icons.get("emojiIcon");
    assertEquals("emoji", icon.getType());
    assertEquals("😃", icon.getEmoji());
  }

  @Test
  void testCustomEmojiIcon() {
    assertNotNull(icons.get("customEmoji"));
    Icon icon = icons.get("customEmoji");
    assertEquals("custom_emoji", icon.getType());
    assertNotNull(icon.getCustomEmoji());
    assertEquals("24cc5b96-8ec4-809e-ba44-007a9a9a34f4", icon.getCustomEmoji().getId());
    assertEquals("sdk_5242016", icon.getCustomEmoji().getName());
    assertEquals(
        "https://s3-us-west-2.amazonaws.com/public.notion-static.com/2f5eb3ad-a4ce-4fb2-8614-efdf80e6ed45/sdk_5242016.png",
        icon.getCustomEmoji().getUrl());
  }

  @Test
  void testExternalIcon() {
    assertNotNull(icons.get("iconFromNotionEmojiPack"));
    Icon icon = icons.get("iconFromNotionEmojiPack");
    assertEquals("external", icon.getType());
    assertNotNull(icon.getExternal());
    assertEquals("https://www.notion.so/icons/light-bulb_purple.svg", icon.getExternal().getUrl());
  }

  @Test
  void testFileIcon() {
    assertNotNull(icons.get("uploadedIcon"));
    Icon icon = icons.get("uploadedIcon");
    assertEquals("file", icon.getType());
    assertNotNull(icon.getFile());
    assertEquals("https://prod-files...", icon.getFile().getUrl());
    assertEquals("2025-08-11T15:25:10.141Z", icon.getFile().getExpiryTime());
  }
}
