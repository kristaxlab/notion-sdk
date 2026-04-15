package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.fluent.NotionBlocks;
import io.kristaxlab.notion.model.common.Color;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ListItemBlockTest {

  // BulletedListItemBlock

  @Test
  @DisplayName("bullets item constructor sets type and inner")
  void bulletsItem_constructor_setsTypeAndInner() {
    BulletedListItemBlock block = new BulletedListItemBlock();

    assertEquals("bulleted_list_item", block.getType());
    assertNotNull(block.getBulletedListItem());
  }

  @Test
  @DisplayName("bullets item of sets rich text")
  void bulletsItem_of_setsRichText() {
    BulletedListItemBlock block = NotionBlocks.bullet("Item 1");

    assertEquals("bulleted_list_item", block.getType());
    assertEquals(1, block.getBulletedListItem().getRichText().size());
    assertEquals("Item 1", block.getBulletedListItem().getRichText().get(0).getPlainText());
  }

  @Test
  @DisplayName("bullets item builder with text")
  void bulletsItem_builder_withText() {
    BulletedListItemBlock block = BulletedListItemBlock.builder().text("Built item").build();

    assertEquals("Built item", block.getBulletedListItem().getRichText().get(0).getPlainText());
  }

  @Test
  @DisplayName("bullets item builder with color")
  void bulletsItem_builder_withColor() {
    BulletedListItemBlock block =
        BulletedListItemBlock.builder().text("Colored").blockColor(Color.ORANGE).build();

    assertEquals("orange", block.getBulletedListItem().getColor());
  }

  @Test
  @DisplayName("bullets item builder with children")
  void bulletsItem_builder_withChildren() {
    BulletedListItemBlock block =
        BulletedListItemBlock.builder().text("Parent").children(c -> c.bullet("Sub-item")).build();

    assertNotNull(block.getBulletedListItem().getChildren());
    assertEquals(1, block.getBulletedListItem().getChildren().size());
  }

  // NumberedListItemBlock

  @Test
  @DisplayName("numbered item constructor sets type and inner")
  void numberedItem_constructor_setsTypeAndInner() {
    NumberedListItemBlock block = new NumberedListItemBlock();

    assertEquals("numbered_list_item", block.getType());
    assertNotNull(block.getNumberedListItem());
  }

  @Test
  @DisplayName("numbered item factory sets rich text")
  void numberedItem_factory_setsRichText() {
    NumberedListItemBlock block = NotionBlocks.numbered("Step 1");

    assertEquals("numbered_list_item", block.getType());
    assertEquals("Step 1", block.getNumberedListItem().getRichText().get(0).getPlainText());
  }

  @Test
  @DisplayName("numbered item builder with text")
  void numberedItem_builder_withText() {
    NumberedListItemBlock block = NumberedListItemBlock.builder().text("Built step").build();

    assertEquals("Built step", block.getNumberedListItem().getRichText().get(0).getPlainText());
  }

  @Test
  @DisplayName("numbered item builder with color and children")
  void numberedItem_builder_withColorAndChildren() {
    NumberedListItemBlock block =
        NumberedListItemBlock.builder()
            .text("Parent")
            .blockColor(Color.GREEN)
            .children(c -> c.numbered("Sub-step"))
            .build();

    assertEquals("green", block.getNumberedListItem().getColor());
    assertNotNull(block.getNumberedListItem().getChildren());
    assertEquals(1, block.getNumberedListItem().getChildren().size());
  }

  @Test
  @DisplayName("numbered item builder with children list")
  void numberedItem_builder_withChildrenList() {
    List<Block> children = NotionBlocks.paragraphList("Child");

    NumberedListItemBlock block =
        NumberedListItemBlock.builder().text("Parent").children(children).build();

    assertEquals(1, block.getNumberedListItem().getChildren().size());
  }
}
