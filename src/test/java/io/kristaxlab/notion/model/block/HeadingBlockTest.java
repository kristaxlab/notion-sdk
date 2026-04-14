package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.fluent.NotionBlocks;
import io.kristaxlab.notion.model.common.Color;
import org.junit.jupiter.api.Test;

class HeadingBlockTest {

  // HeadingOneBlock

  @Test
  void headingOne_constructor_setsTypeAndHeading() {
    HeadingOneBlock block = new HeadingOneBlock();

    assertEquals("heading_1", block.getType());
    assertNotNull(block.getHeading1());
  }

  @Test
  void headingOne_factory_setsRichText() {
    HeadingOneBlock block = NotionBlocks.heading1("Title");

    assertEquals("heading_1", block.getType());
    assertEquals(1, block.getHeading1().getRichText().size());
    assertEquals("Title", block.getHeading1().getRichText().get(0).getPlainText());
  }

  @Test
  void headingOne_builder_withText() {
    HeadingOneBlock block = HeadingOneBlock.builder().text("Built Title").build();

    assertEquals("heading_1", block.getType());
    assertEquals("Built Title", block.getHeading1().getRichText().get(0).getPlainText());
  }

  @Test
  void headingOne_builder_withToggleable() {
    HeadingOneBlock block =
        HeadingOneBlock.builder().text("Toggleable Heading").toggleable(true).build();

    assertTrue(block.getHeading1().getIsToggleable());
  }

  @Test
  void headingOne_builder_toggleableNotSet_isToggleableNull() {
    HeadingOneBlock block = HeadingOneBlock.builder().text("Not toggleable").build();

    assertNull(block.getHeading1().getIsToggleable());
  }

  @Test
  void headingOne_builder_withColor() {
    HeadingOneBlock block = HeadingOneBlock.builder().text("Colored").blockColor(Color.RED).build();

    assertEquals("red", block.getHeading1().getColor());
  }

  @Test
  void headingOne_builder_withChildren() {
    HeadingOneBlock block =
        HeadingOneBlock.builder()
            .text("Parent Heading")
            .children(c -> c.paragraph("Child content"))
            .build();

    assertNotNull(block.getHeading1().getChildren());
    assertEquals(1, block.getHeading1().getChildren().size());
  }

  // HeadingTwoBlock

  @Test
  void headingTwo_constructor_setsTypeAndHeading() {
    HeadingTwoBlock block = new HeadingTwoBlock();

    assertEquals("heading_2", block.getType());
    assertNotNull(block.getHeading2());
  }

  @Test
  void headingTwo_factory_setsRichText() {
    HeadingTwoBlock block = NotionBlocks.heading2("Subtitle");

    assertEquals("heading_2", block.getType());
    assertEquals("Subtitle", block.getHeading2().getRichText().get(0).getPlainText());
  }

  @Test
  void headingTwo_builder_withTextAndToggleable() {
    HeadingTwoBlock block = HeadingTwoBlock.builder().text("Toggle H2").toggleable(true).build();

    assertEquals("heading_2", block.getType());
    assertTrue(block.getHeading2().getIsToggleable());
  }

  @Test
  void headingTwo_builder_withColor() {
    HeadingTwoBlock block =
        HeadingTwoBlock.builder().text("Blue H2").blockColor(Color.BLUE).build();

    assertEquals("blue", block.getHeading2().getColor());
  }

  // HeadingThreeBlock

  @Test
  void headingThree_constructor_setsTypeAndHeading() {
    HeadingThreeBlock block = new HeadingThreeBlock();

    assertEquals("heading_3", block.getType());
    assertNotNull(block.getHeading3());
  }

  @Test
  void headingThree_factory_setsRichText() {
    HeadingThreeBlock block = NotionBlocks.heading3("Section");

    assertEquals("heading_3", block.getType());
    assertEquals("Section", block.getHeading3().getRichText().get(0).getPlainText());
  }

  @Test
  void headingThree_builder_withTextAndToggleable() {
    HeadingThreeBlock block =
        HeadingThreeBlock.builder().text("Toggle H3").toggleable(false).build();

    assertEquals("heading_3", block.getType());
    assertFalse(block.getHeading3().getIsToggleable());
  }

  // HeadingFourBlock

  @Test
  void headingFour_constructor_setsTypeAndHeading() {
    HeadingFourBlock block = new HeadingFourBlock();

    assertEquals("heading_4", block.getType());
    assertNotNull(block.getHeading4());
  }

  @Test
  void headingFour_factory_setsRichText() {
    HeadingFourBlock block = NotionBlocks.heading4("Sub-section");

    assertEquals("heading_4", block.getType());
    assertEquals("Sub-section", block.getHeading4().getRichText().get(0).getPlainText());
  }

  @Test
  void headingFour_builder_withTextAndToggleable() {
    HeadingFourBlock block = HeadingFourBlock.builder().text("Toggle H4").toggleable(true).build();

    assertEquals("heading_4", block.getType());
    assertTrue(block.getHeading4().getIsToggleable());
  }

  @Test
  void headingFour_builder_withColor() {
    HeadingFourBlock block =
        HeadingFourBlock.builder().text("Styled H4").blockColor(Color.PURPLE).build();

    assertEquals("purple", block.getHeading4().getColor());
  }

  // Heading inner class

  @Test
  void heading_getterSetter_isToggleable() {
    Heading heading = new Heading();

    assertNull(heading.getIsToggleable());
    heading.setIsToggleable(true);
    assertTrue(heading.getIsToggleable());
  }
}
