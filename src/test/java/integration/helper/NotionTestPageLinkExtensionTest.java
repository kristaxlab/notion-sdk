package integration.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import integration.NotionIntegrationTestsExtension;
import org.junit.jupiter.api.Test;

class NotionIntegrationTestsExtensionTest {

  @Test
  void toNotionPageUrlRemovesHyphensFromPageId() {
    assertEquals(
        "https://www.notion.so/1234567890abcdef1234567890abcdef",
        NotionIntegrationTestsExtension.toNotionPageUrl("12345678-90ab-cdef-1234-567890abcdef"));
  }
}
