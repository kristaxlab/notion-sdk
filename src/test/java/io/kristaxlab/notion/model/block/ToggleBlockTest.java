package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.fluent.NotionBlocks;
import io.kristaxlab.notion.model.common.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ToggleBlockTest {

  @Test
  @DisplayName("constructor sets type and initializes toggle")
  void constructor_setsTypeAndInitializesToggle() {
    ToggleBlock block = new ToggleBlock();

    assertEquals("toggle", block.getType());
    assertNotNull(block.getToggle());
  }

  @Test
  @DisplayName("toggle sets rich text")
  void toggle_setsRichText() {
    ToggleBlock block = NotionBlocks.toggle("Click to expand");

    assertEquals("toggle", block.getType());
    assertEquals(1, block.getToggle().getRichText().size());
    assertEquals("Click to expand", block.getToggle().getRichText().get(0).getPlainText());
  }

  @Test
  @DisplayName("builder with text")
  void builder_withText() {
    ToggleBlock block = ToggleBlock.builder().text("Toggle text").build();

    assertEquals("Toggle text", block.getToggle().getRichText().get(0).getPlainText());
  }

  @Test
  @DisplayName("builder with color")
  void builder_withColor() {
    ToggleBlock block = ToggleBlock.builder().text("Colored").blockColor(Color.BROWN).build();

    assertEquals("brown", block.getToggle().getColor());
  }

  @Test
  @DisplayName("builder with children")
  void builder_withChildren() {
    ToggleBlock block =
        ToggleBlock.builder()
            .text("Parent")
            .children(c -> c.paragraph("Hidden content").bullet("Item"))
            .build();

    assertNotNull(block.getToggle().getChildren());
    assertEquals(2, block.getToggle().getChildren().size());
  }
}
