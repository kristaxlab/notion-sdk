package io.kristixlab.notion.api.model.block;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.common.Color;
import io.kristixlab.notion.api.model.helper.NotionBlocks;
import org.junit.jupiter.api.Test;

class ToggleBlockTest {

  @Test
  void constructor_setsTypeAndInitializesToggle() {
    ToggleBlock block = new ToggleBlock();

    assertEquals("toggle", block.getType());
    assertNotNull(block.getToggle());
  }

  @Test
  void toggle_setsRichText() {
    ToggleBlock block = NotionBlocks.toggle("Click to expand");

    assertEquals("toggle", block.getType());
    assertEquals(1, block.getToggle().getRichText().size());
    assertEquals("Click to expand", block.getToggle().getRichText().get(0).getPlainText());
  }

  @Test
  void builder_withText() {
    ToggleBlock block = ToggleBlock.builder().text("Toggle text").build();

    assertEquals("Toggle text", block.getToggle().getRichText().get(0).getPlainText());
  }

  @Test
  void builder_withColor() {
    ToggleBlock block = ToggleBlock.builder().text("Colored").blockColor(Color.BROWN).build();

    assertEquals("brown", block.getToggle().getColor());
  }

  @Test
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
