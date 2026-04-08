package io.kristixlab.notion.api.model.blocks;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.common.Color;
import java.util.List;
import org.junit.jupiter.api.Test;

class ListItemBlockTest {

  // BulletedListItemBlock

  @Test
  void bulletedListItem_constructor_setsTypeAndInner() {
    BulletedListItemBlock block = new BulletedListItemBlock();

    assertEquals("bulleted_list_item", block.getType());
    assertNotNull(block.getBulletedListItem());
  }

  @Test
  void bulletedListItem_of_setsRichText() {
    BulletedListItemBlock block = BulletedListItemBlock.of("Item 1");

    assertEquals("bulleted_list_item", block.getType());
    assertEquals(1, block.getBulletedListItem().getRichText().size());
    assertEquals("Item 1", block.getBulletedListItem().getRichText().get(0).getPlainText());
  }

  @Test
  void bulletedListItem_builder_withText() {
    BulletedListItemBlock block = BulletedListItemBlock.builder().text("Built item").build();

    assertEquals("Built item", block.getBulletedListItem().getRichText().get(0).getPlainText());
  }

  @Test
  void bulletedListItem_builder_withColor() {
    BulletedListItemBlock block =
        BulletedListItemBlock.builder().text("Colored").color(Color.ORANGE).build();

    assertEquals("orange", block.getBulletedListItem().getColor());
  }

  @Test
  void bulletedListItem_builder_withChildren() {
    BulletedListItemBlock block =
        BulletedListItemBlock.builder()
            .text("Parent")
            .children(c -> c.bulletedListItem("Sub-item"))
            .build();

    assertNotNull(block.getBulletedListItem().getChildren());
    assertEquals(1, block.getBulletedListItem().getChildren().size());
  }

  @Test
  void bulletedListItem_builder_withAnnotations() {
    BulletedListItemBlock block =
        BulletedListItemBlock.builder().text("Bold item").bold().strikethrough().build();

    assertTrue(block.getBulletedListItem().getRichText().get(0).getAnnotations().getBold());
    assertTrue(
        block.getBulletedListItem().getRichText().get(0).getAnnotations().getStrikethrough());
  }

  // NumberedListItemBlock

  @Test
  void numberedListItem_constructor_setsTypeAndInner() {
    NumberedListItemBlock block = new NumberedListItemBlock();

    assertEquals("numbered_list_item", block.getType());
    assertNotNull(block.getNumberedListItem());
  }

  @Test
  void numberedListItem_of_setsRichText() {
    NumberedListItemBlock block = NumberedListItemBlock.of("Step 1");

    assertEquals("numbered_list_item", block.getType());
    assertEquals("Step 1", block.getNumberedListItem().getRichText().get(0).getPlainText());
  }

  @Test
  void numberedListItem_builder_withText() {
    NumberedListItemBlock block = NumberedListItemBlock.builder().text("Built step").build();

    assertEquals("Built step", block.getNumberedListItem().getRichText().get(0).getPlainText());
  }

  @Test
  void numberedListItem_builder_withColorAndChildren() {
    NumberedListItemBlock block =
        NumberedListItemBlock.builder()
            .text("Parent")
            .color(Color.GREEN)
            .children(c -> c.numberedListItem("Sub-step"))
            .build();

    assertEquals("green", block.getNumberedListItem().getColor());
    assertNotNull(block.getNumberedListItem().getChildren());
    assertEquals(1, block.getNumberedListItem().getChildren().size());
  }

  @Test
  void numberedListItem_builder_withChildrenList() {
    List<Block> children = List.of(ParagraphBlock.of("Child"));

    NumberedListItemBlock block =
        NumberedListItemBlock.builder().text("Parent").children(children).build();

    assertEquals(1, block.getNumberedListItem().getChildren().size());
  }
}
