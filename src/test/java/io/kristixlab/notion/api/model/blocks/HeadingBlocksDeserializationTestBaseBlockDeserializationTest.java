package io.kristixlab.notion.api.model.blocks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristixlab.notion.api.model.common.RichText;
import org.junit.jupiter.api.Test;

public class HeadingBlocksDeserializationTestBaseBlockDeserializationTest
    extends BaseBlockDeserializationTest {

  @Test
  void testHeadingOne() {
    Block b = findBlockById("22dc5b96-8ec4-8027-bf8f-ec4de9092faa");
    assertNotNull(b);

    HeadingOneBlock h = (HeadingOneBlock) b;
    assertEquals("block", h.getObject());
    assertEquals("heading_1", h.getType());
    assertFalse(h.hasChildren());
    assertFalse(h.isArchived());
    assertFalse(h.isInTrash());
    assertParent(h, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-11T18:10:00.000Z", h.getCreatedTime());
    assertEquals("2025-07-11T18:10:00.000Z", h.getLastEditedTime());
    assertNotNull(h.getCreatedBy());
    assertEquals("user", h.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", h.getCreatedBy().getId());
    assertNotNull(h.getLastEditedBy());
    assertEquals("user", h.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", h.getLastEditedBy().getId());
    assertNotNull(h.getHeading1());
    assertEquals("default", h.getHeading1().getColor());
    assertFalse(h.getHeading1().isToggleable());
    assertNotNull(h.getHeading1().getRichText());
    assertEquals(1, h.getHeading1().getRichText().size());
    RichText text = h.getHeading1().getRichText().get(0);
    assertEquals("text", text.getType());
    assertNotNull(text.getText());
    assertEquals("Basic blocks", text.getText().getContent());
    assertNull(text.getText().getLink());
    assertNotNull(text.getAnnotations());
    assertFalse(text.getAnnotations().isBold());
    assertFalse(text.getAnnotations().isItalic());
    assertFalse(text.getAnnotations().isStrikethrough());
    assertFalse(text.getAnnotations().isUnderline());
    assertFalse(text.getAnnotations().isCode());
    assertEquals("default", text.getAnnotations().getColor());
    assertEquals("Basic blocks", text.getPlainText());
    assertNull(text.getHref());
  }

  @Test
  void testHeadingTwo() {
    Block b = findBlockById("22dc5b96-8ec4-8011-9d64-c05ba1433ae8");
    assertNotNull(b);

    HeadingTwoBlock h = (HeadingTwoBlock) b;
    assertEquals("block", h.getObject());
    assertEquals("heading_2", h.getType());
    assertFalse(h.hasChildren());
    assertFalse(h.isArchived());
    assertFalse(h.isInTrash());
    assertParent(h, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-11T17:59:00.000Z", h.getCreatedTime());
    assertEquals("2025-07-11T18:00:00.000Z", h.getLastEditedTime());
    assertNotNull(h.getCreatedBy());
    assertEquals("user", h.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", h.getCreatedBy().getId());
    assertNotNull(h.getLastEditedBy());
    assertEquals("user", h.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", h.getLastEditedBy().getId());
    assertNotNull(h.getHeading2());
    assertEquals("default", h.getHeading2().getColor());
    assertFalse(h.getHeading2().isToggleable());
    assertNotNull(h.getHeading2().getRichText());
    assertEquals(1, h.getHeading2().getRichText().size());
    RichText text = h.getHeading2().getRichText().get(0);
    assertEquals("text", text.getType());
    assertNotNull(text.getText());
    assertEquals("Heading 2 here", text.getText().getContent());
    assertNull(text.getText().getLink());
    assertNotNull(text.getAnnotations());
    assertFalse(text.getAnnotations().isBold());
    assertFalse(text.getAnnotations().isItalic());
    assertFalse(text.getAnnotations().isStrikethrough());
    assertFalse(text.getAnnotations().isUnderline());
    assertFalse(text.getAnnotations().isCode());
    assertEquals("default", text.getAnnotations().getColor());
    assertEquals("Heading 2 here", text.getPlainText());
    assertNull(text.getHref());
  }

  @Test
  void test5HeadingThree() {
    Block b = findBlockById("22dc5b96-8ec4-8027-b579-cb785233a59a");
    assertNotNull(b);

    HeadingThreeBlock h = (HeadingThreeBlock) b;
    assertEquals("block", h.getObject());
    assertEquals("heading_3", h.getType());
    assertFalse(h.hasChildren());
    assertFalse(h.isArchived());
    assertFalse(h.isInTrash());
    assertParent(h, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-11T18:00:00.000Z", h.getCreatedTime());
    assertEquals("2025-07-11T18:00:00.000Z", h.getLastEditedTime());
    assertNotNull(h.getCreatedBy());
    assertEquals("user", h.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", h.getCreatedBy().getId());
    assertNotNull(h.getLastEditedBy());
    assertEquals("user", h.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", h.getLastEditedBy().getId());
    assertNotNull(h.getHeading3());
    assertEquals("default", h.getHeading3().getColor());
    assertFalse(h.getHeading3().isToggleable());
    assertNotNull(h.getHeading3().getRichText());
    assertEquals(1, h.getHeading3().getRichText().size());
    RichText text = h.getHeading3().getRichText().get(0);
    assertEquals("text", text.getType());
    assertNotNull(text.getText());
    assertEquals("Heading 3 here", text.getText().getContent());
    assertNull(text.getText().getLink());
    assertNotNull(text.getAnnotations());
    assertFalse(text.getAnnotations().isBold());
    assertFalse(text.getAnnotations().isItalic());
    assertFalse(text.getAnnotations().isStrikethrough());
    assertFalse(text.getAnnotations().isUnderline());
    assertFalse(text.getAnnotations().isCode());
    assertEquals("default", text.getAnnotations().getColor());
    assertEquals("Heading 3 here", text.getPlainText());
    assertNull(text.getHref());
  }

  @Test
  void testHeadingOneToggled() {
    Block b = findBlockById("22dc5b96-8ec4-8068-b563-ebc444f11047");
    assertNotNull(b);

    HeadingOneBlock h = (HeadingOneBlock) b;
    assertEquals("block", h.getObject());
    assertEquals("heading_1", h.getType());
    assertTrue(h.hasChildren());
    assertFalse(h.isArchived());
    assertFalse(h.isInTrash());
    assertParent(h, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-11T18:01:00.000Z", h.getCreatedTime());
    assertEquals("2025-07-11T18:01:00.000Z", h.getLastEditedTime());
    assertNotNull(h.getCreatedBy());
    assertEquals("user", h.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", h.getCreatedBy().getId());
    assertNotNull(h.getLastEditedBy());
    assertEquals("user", h.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", h.getLastEditedBy().getId());
    assertNotNull(h.getHeading1());
    assertEquals("default", h.getHeading1().getColor());
    assertTrue(h.getHeading1().isToggleable());
    assertNotNull(h.getHeading1().getRichText());
    assertEquals(1, h.getHeading1().getRichText().size());
    RichText text = h.getHeading1().getRichText().get(0);
    assertEquals("text", text.getType());
    assertNotNull(text.getText());
    assertEquals("Heading 1 here toggle", text.getText().getContent());
  }

  @Test
  void testHeadingTwoToggled() {
    Block b = findBlockById("22dc5b96-8ec4-8046-a00f-d552949a69e6");
    assertNotNull(b);

    HeadingTwoBlock h = (HeadingTwoBlock) b;
    assertEquals("block", h.getObject());
    assertEquals("heading_2", h.getType());
    assertTrue(h.hasChildren());
    assertFalse(h.isArchived());
    assertFalse(h.isInTrash());
    assertParent(h, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-11T18:01:00.000Z", h.getCreatedTime());
    assertEquals("2025-07-11T18:02:00.000Z", h.getLastEditedTime());
    assertNotNull(h.getCreatedBy());
    assertEquals("user", h.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", h.getCreatedBy().getId());
    assertNotNull(h.getLastEditedBy());
    assertEquals("user", h.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", h.getLastEditedBy().getId());
    assertNotNull(h.getHeading2());
    assertEquals("default", h.getHeading2().getColor());
    assertTrue(h.getHeading2().isToggleable());
    assertNotNull(h.getHeading2().getRichText());
    assertEquals(1, h.getHeading2().getRichText().size());
    RichText text = h.getHeading2().getRichText().get(0);
    assertEquals("text", text.getType());
    assertNotNull(text.getText());
    assertEquals("Heading 2 here toggle", text.getText().getContent());
    assertNull(text.getText().getLink());
    assertEquals("Heading 2 here toggle", text.getPlainText());
    assertNull(text.getHref());
    assertNotNull(text.getAnnotations());
    assertFalse(text.getAnnotations().isBold());
    assertFalse(text.getAnnotations().isItalic());
    assertFalse(text.getAnnotations().isStrikethrough());
    assertFalse(text.getAnnotations().isUnderline());
    assertFalse(text.getAnnotations().isCode());
    assertEquals("default", text.getAnnotations().getColor());
  }

  @Test
  void testHeadingThreeToggled() {
    Block b = findBlockById("22dc5b96-8ec4-80d3-a804-ee3399863a71");
    assertNotNull(b);

    HeadingThreeBlock h = (HeadingThreeBlock) b;
    assertEquals("block", h.getObject());
    assertEquals("heading_3", h.getType());
    assertTrue(h.hasChildren());
    assertFalse(h.isArchived());
    assertFalse(h.isInTrash());
    assertParent(h, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-11T18:02:00.000Z", h.getCreatedTime());
    assertEquals("2025-07-11T18:02:00.000Z", h.getLastEditedTime());
    assertNotNull(h.getCreatedBy());
    assertEquals("user", h.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", h.getCreatedBy().getId());
    assertNotNull(h.getLastEditedBy());
    assertEquals("user", h.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", h.getLastEditedBy().getId());
    assertNotNull(h.getHeading3());
    assertEquals("default", h.getHeading3().getColor());
    assertTrue(h.getHeading3().isToggleable());
    assertNotNull(h.getHeading3().getRichText());
    assertEquals(1, h.getHeading3().getRichText().size());
    RichText text = h.getHeading3().getRichText().get(0);
    assertEquals("text", text.getType());
    assertNotNull(text.getText());
    assertEquals("Heading 3 here toggle", text.getText().getContent());
    assertNull(text.getText().getLink());
    assertEquals("Heading 3 here toggle", text.getPlainText());
    assertNull(text.getHref());
    assertNotNull(text.getAnnotations());
    assertFalse(text.getAnnotations().isBold());
    assertFalse(text.getAnnotations().isItalic());
    assertFalse(text.getAnnotations().isStrikethrough());
    assertFalse(text.getAnnotations().isUnderline());
    assertFalse(text.getAnnotations().isCode());
    assertEquals("default", text.getAnnotations().getColor());
  }
}
