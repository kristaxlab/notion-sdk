package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import io.kristaxlab.notion.fluent.NotionBlocks;
import io.kristaxlab.notion.http.base.json.JacksonSerializer;
import io.kristaxlab.notion.model.common.Icon;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TabBlockTest {

  private static final JacksonSerializer JSON = JacksonSerializer.withDefaults();

  @Test
  @DisplayName("constructor sets type and initializes empty tab payload")
  void constructor_setsTypeAndInitializesTab() {
    TabBlock block = new TabBlock();

    assertEquals("tab", block.getType());
    assertNotNull(block.getTab());
    assertNull(block.getTab().getChildren());
  }

  @Test
  @DisplayName("deserialize empty tab payload yields TabBlock not UnknownBlock")
  void deserialize_emptyTabPayload_yieldsTabBlock() {
    String json =
        """
        {
          "object": "block",
          "id": "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee",
          "type": "tab",
          "has_children": true,
          "tab": {}
        }
        """;

    Block block = JSON.toObject(json, Block.class);

    assertInstanceOf(TabBlock.class, block);
    assertEquals("tab", block.getType());
    assertTrue(block.getHasChildren());
    assertNotNull(block.asTab().getTab());
    assertNull(block.asTab().getTab().getChildren());
  }

  @Test
  @DisplayName("serialize create payload includes paragraph children under tab")
  void serialize_createPayload_includesParagraphChildren() {
    TabBlock block =
        NotionBlocks.tab(
            ParagraphBlock.builder()
                .text("Overview")
                .icon(Icon.emoji("📋"))
                .children(c -> c.paragraph("Tab 1 content"))
                .build(),
            ParagraphBlock.builder()
                .text("Details")
                .children(c -> c.paragraph("Tab 2 content"))
                .build());

    String json = JSON.toJson(block);
    JsonNode root = JSON.toObject(json, JsonNode.class);

    assertEquals("tab", root.get("type").asText());
    assertTrue(root.get("tab").has("children"));
    assertEquals(2, root.get("tab").get("children").size());
    assertEquals("paragraph", root.get("tab").get("children").get(0).get("type").asText());
    assertEquals(
        "Overview",
        root.get("tab")
            .get("children")
            .get(0)
            .get("paragraph")
            .get("rich_text")
            .get(0)
            .get("text")
            .get("content")
            .asText());
    assertEquals(
        "📋",
        root.get("tab").get("children").get(0).get("paragraph").get("icon").get("emoji").asText());
  }

  @Test
  @DisplayName("serialize empty tab payload is empty object")
  void serialize_emptyTab_isEmptyObject() {
    TabBlock block = new TabBlock();

    String json = JSON.toJson(block);
    JsonNode root = JSON.toObject(json, JsonNode.class);

    assertEquals("tab", root.get("type").asText());
    assertTrue(root.get("tab").isObject());
    assertEquals(0, root.get("tab").size());
  }

  @Test
  @DisplayName("tab children getter setter")
  void tab_childrenGetterSetter() {
    TabBlock.Tab tab = new TabBlock.Tab();
    List<ParagraphBlock> children = List.of(NotionBlocks.paragraph("Label"));

    assertNull(tab.getChildren());
    tab.setChildren(children);
    assertSame(children, tab.getChildren());
  }
}
