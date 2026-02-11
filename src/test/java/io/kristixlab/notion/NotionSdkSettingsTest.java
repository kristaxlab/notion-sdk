package io.kristixlab.notion;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class NotionSdkSettingsTest {

  @Test
  void getValue() {
    assertNotNull(NotionSdkSettings.getInstance().getValue("notion.test.double"));
    assertNotNull(NotionSdkSettings.getInstance().getValue("notion.test"));
    assertNotNull(NotionSdkSettings.getInstance().getValue("notion"));
    assertNull(NotionSdkSettings.getInstance().getValue("notion.test.double.nonexistent"));
    assertNull(NotionSdkSettings.getInstance().getValue("notion.val.double"));
    assertNull(NotionSdkSettings.getInstance().getValue("val.val.double"));
    assertNull(NotionSdkSettings.getInstance().getValue("double"));

    assertInstanceOf(Double.class, NotionSdkSettings.getInstance().getValue("notion.test.double"));
    assertInstanceOf(List.class, NotionSdkSettings.getInstance().getValue("notion.test.list"));
    assertInstanceOf(Map.class, NotionSdkSettings.getInstance().getValue("notion.test.map"));
    assertInstanceOf(Map.class, NotionSdkSettings.getInstance().getValue("notion"));
  }

  /*
   * This test validates that values in notion-sdk-settings.properties can override the default values in
   * notion-sdk-settings-default.properties.
   */
  @Test
  void getOverridenValue() {
    int intVal =
        NotionSdkSettings.getInstance()
            .getInteger("notion.endpoints.file-uploads.stream-after-bytes");

    assertEquals(5242880, intVal);
  }

  @Test
  void getString() {
    String strVal = NotionSdkSettings.getInstance().getString("notion.test.string");
    assertEquals("Hello, Notion SDK!", strVal);

    String nonExistStrVal =
        NotionSdkSettings.getInstance().getString("notion.test.string.nonexistent");
    assertNull(nonExistStrVal);
  }

  @Test
  void getInteger() {
    int intVal = NotionSdkSettings.getInstance().getInteger("notion.test.integer");
    assertEquals(42, intVal);

    Integer nonExistIntVal =
        NotionSdkSettings.getInstance().getInteger("notion.test.integer.nonexistent");
    assertNull(nonExistIntVal);
  }

  @Test
  void getLong() {
    Long longVal = NotionSdkSettings.getInstance().getLong("notion.test.long");
    assertEquals(1234567890123456789L, longVal);

    Long nonExistLongVal = NotionSdkSettings.getInstance().getLong("notion.test.long.nonexistent");
    assertNull(nonExistLongVal);
  }

  @Test
  void getDouble() {
    Double doubleVal = NotionSdkSettings.getInstance().getDouble("notion.test.double");
    assertNotNull(doubleVal);
    assertEquals(3.14159, doubleVal);

    Double nonExistDoubleVal =
        NotionSdkSettings.getInstance().getDouble("notion.test.double.nonexistent");
    assertNull(nonExistDoubleVal);
  }

  @Test
  void getBoolean() {
    Boolean boolVal = NotionSdkSettings.getInstance().getBoolean("notion.test.boolean");
    assertNotNull(boolVal);
    assertTrue(boolVal);

    Boolean nonExistBoolVal =
        NotionSdkSettings.getInstance().getBoolean("notion.test.boolean.nonexistent");
    assertNull(nonExistBoolVal);
  }

  @Test
  void getList() {
    List<String> list = NotionSdkSettings.getInstance().getList("notion.test.list");
    assertEquals(3, list.size());
    for (int i = 0; i < list.size(); i++) {
      assertEquals("value" + (i + 1), list.get(i));
    }
  }

  @Test
  void getMap() {
    Map<String, String> map = NotionSdkSettings.getInstance().getMap("notion.test.map");
    assertEquals(3, map.size());
    for (int i = 0; i < map.size(); i++) {
      assertEquals("value" + (i + 1), map.get("key" + (i + 1)));
    }
  }

  @Test
  void exists() {
    assertFalse(NotionSdkSettings.getInstance().exists("notion.test.nonexistent"));
    assertTrue(NotionSdkSettings.getInstance().exists("notion.test.list"));
    assertTrue(NotionSdkSettings.getInstance().exists("notion"));
  }
}
