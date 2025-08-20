package io.kristixlab.notion.api.model.blocks;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristixlab.notion.api.model.common.RichText;
import org.junit.jupiter.api.Test;

public class TextBlocksDeserializationTest extends BaseBlockDeserializationTest {

  @Test
  void testParagraphEmpty() {
    Block b = findBlockById("22ec5b96-8ec4-8056-bff6-f1436bf9f7a8");
    assertNotNull(b);

    ParagraphBlock p = b.asParagraph();
    // Block-level
    assertEquals("block", p.getObject());
    assertEquals("22ec5b96-8ec4-8056-bff6-f1436bf9f7a8", p.getId());
    assertEquals("paragraph", p.getType());
    assertEquals("2025-07-12T17:40:00.000Z", p.getCreatedTime());
    assertEquals("2025-07-12T17:40:00.000Z", p.getLastEditedTime());
    assertFalse(p.getArchived());
    assertFalse(p.getInTrash());
    assertFalse(p.hasChildren());
    assertParent(p, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");

    // Created by
    assertNotNull(p.getCreatedBy());
    assertEquals("user", p.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", p.getCreatedBy().getId());

    // Last edited by
    assertNotNull(p.getLastEditedBy());
    assertEquals("user", p.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", p.getLastEditedBy().getId());

    // Paragraph
    assertNotNull(p.getParagraph());
    assertEquals("default", p.getParagraph().getColor());
    assertNotNull(p.getParagraph().getRichText());
    assertEquals(0, p.getParagraph().getRichText().size());
  }

  @Test
  void testParagraphWithText() {
    Block b = findBlockById("22dc5b96-8ec4-8061-8a09-c5c8d4ce2ab4");
    assertNotNull(b);

    ParagraphBlock p = b.asParagraph();
    assertEquals("block", p.getObject());
    assertEquals("22dc5b96-8ec4-8061-8a09-c5c8d4ce2ab4", p.getId());
    assertEquals("paragraph", p.getType());
    assertFalse(p.hasChildren());
    assertFalse(p.getArchived());
    assertFalse(p.getInTrash());
    assertParent(p, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-11T17:59:00.000Z", p.getCreatedTime());
    assertEquals("2025-07-11T17:59:00.000Z", p.getLastEditedTime());
    assertNotNull(p.getCreatedBy());
    assertEquals("user", p.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", p.getCreatedBy().getId());
    assertNotNull(p.getLastEditedBy());
    assertEquals("user", p.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", p.getLastEditedBy().getId());
    assertNotNull(p.getParagraph());
    assertEquals("default", p.getParagraph().getColor());
    assertNotNull(p.getParagraph().getRichText());
    assertEquals(1, p.getParagraph().getRichText().size());
    RichText text = p.getParagraph().getRichText().get(0);
    assertEquals("text", text.getType());
    assertNotNull(text.getText());
    assertEquals("This is a simple text", text.getText().getContent());
    assertNull(text.getText().getLink());
    assertNotNull(text.getAnnotations());
    assertFalse(text.getAnnotations().isBold());
    assertFalse(text.getAnnotations().isItalic());
    assertFalse(text.getAnnotations().isStrikethrough());
    assertFalse(text.getAnnotations().isUnderline());
    assertFalse(text.getAnnotations().isCode());
    assertEquals("default", text.getAnnotations().getColor());
    assertEquals("This is a simple text", text.getPlainText());
    assertNull(text.getHref());
  }

  @Test
  void testParagraphWithFormatting() {
    Block b = findBlockById("22dc5b96-8ec4-808b-b354-f59684f54293");
    assertNotNull(b);

    ParagraphBlock p = b.asParagraph();
    // Block-level assertions
    assertEquals("block", p.getObject());
    assertEquals("paragraph", p.getType());
    assertFalse(p.hasChildren());
    assertFalse(p.getArchived());
    assertFalse(p.getInTrash());
    assertParent(p, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-11T17:59:00.000Z", p.getCreatedTime());
    assertEquals("2025-07-11T18:04:00.000Z", p.getLastEditedTime());

    assertNotNull(p.getCreatedBy());
    assertEquals("user", p.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", p.getCreatedBy().getId());

    assertNotNull(p.getLastEditedBy());
    assertEquals("user", p.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", p.getLastEditedBy().getId());

    // Paragraph-level assertions
    assertNotNull(p.getParagraph());
    assertEquals("default", p.getParagraph().getColor());
    assertNotNull(p.getParagraph().getRichText());
    assertEquals(11, p.getParagraph().getRichText().size());

    // RichText 0: "Some more"
    RichText rt0 = p.getParagraph().getRichText().get(0);
    assertEquals("text", rt0.getType());
    assertEquals("Some more", rt0.getText().getContent());
    assertNull(rt0.getText().getLink());
    assertFalse(rt0.getAnnotations().isBold());
    assertFalse(rt0.getAnnotations().isItalic());
    assertFalse(rt0.getAnnotations().isStrikethrough());
    assertFalse(rt0.getAnnotations().isUnderline());
    assertFalse(rt0.getAnnotations().isCode());
    assertEquals("default", rt0.getAnnotations().getColor());
    assertEquals("Some more", rt0.getPlainText());
    assertNull(rt0.getHref());

    // RichText 1: " text with" (italic)
    RichText rt1 = p.getParagraph().getRichText().get(1);
    assertEquals("text", rt1.getType());
    assertEquals(" text with", rt1.getText().getContent());
    assertTrue(rt1.getAnnotations().isItalic());
    assertFalse(rt1.getAnnotations().isBold());
    assertFalse(rt1.getAnnotations().isStrikethrough());
    assertFalse(rt1.getAnnotations().isUnderline());
    assertFalse(rt1.getAnnotations().isCode());
    assertEquals("default", rt1.getAnnotations().getColor());
    assertEquals(" text with", rt1.getPlainText());

    // RichText 2: " "
    RichText rt2 = p.getParagraph().getRichText().get(2);
    assertEquals("text", rt2.getType());
    assertEquals(" ", rt2.getText().getContent());
    assertFalse(rt2.getAnnotations().isBold());
    assertFalse(rt2.getAnnotations().isItalic());
    assertFalse(rt2.getAnnotations().isStrikethrough());
    assertFalse(rt2.getAnnotations().isUnderline());
    assertFalse(rt2.getAnnotations().isCode());
    assertEquals("default", rt2.getAnnotations().getColor());
    assertEquals(" ", rt2.getPlainText());

    // RichText 3: "rich" (underline)
    RichText rt3 = p.getParagraph().getRichText().get(3);
    assertEquals("text", rt3.getType());
    assertEquals("rich", rt3.getText().getContent());
    assertTrue(rt3.getAnnotations().isUnderline());
    assertFalse(rt3.getAnnotations().isBold());
    assertFalse(rt3.getAnnotations().isItalic());
    assertFalse(rt3.getAnnotations().isStrikethrough());
    assertFalse(rt3.getAnnotations().isCode());
    assertEquals("default", rt3.getAnnotations().getColor());
    assertEquals("rich", rt3.getPlainText());

    // RichText 4: " "
    RichText rt4 = p.getParagraph().getRichText().get(4);
    assertEquals("text", rt4.getType());
    assertEquals(" ", rt4.getText().getContent());
    assertFalse(rt4.getAnnotations().isBold());
    assertFalse(rt4.getAnnotations().isItalic());
    assertFalse(rt4.getAnnotations().isStrikethrough());
    assertFalse(rt4.getAnnotations().isUnderline());
    assertFalse(rt4.getAnnotations().isCode());
    assertEquals("default", rt4.getAnnotations().getColor());
    assertEquals(" ", rt4.getPlainText());

    // RichText 5: "text" (code)
    RichText rt5 = p.getParagraph().getRichText().get(5);
    assertEquals("text", rt5.getType());
    assertEquals("text", rt5.getText().getContent());
    assertTrue(rt5.getAnnotations().isCode());
    assertFalse(rt5.getAnnotations().isBold());
    assertFalse(rt5.getAnnotations().isItalic());
    assertFalse(rt5.getAnnotations().isStrikethrough());
    assertFalse(rt5.getAnnotations().isUnderline());
    assertEquals("default", rt5.getAnnotations().getColor());
    assertEquals("text", rt5.getPlainText());

    // RichText 6: ", some more text here." (code, blue)
    RichText rt6 = p.getParagraph().getRichText().get(6);
    assertEquals("text", rt6.getType());
    assertEquals(", some more text here.", rt6.getText().getContent());
    assertTrue(rt6.getAnnotations().isCode());
    assertFalse(rt6.getAnnotations().isBold());
    assertFalse(rt6.getAnnotations().isItalic());
    assertFalse(rt6.getAnnotations().isStrikethrough());
    assertFalse(rt6.getAnnotations().isUnderline());
    assertEquals("blue", rt6.getAnnotations().getColor());
    assertEquals(", some more text here.", rt6.getPlainText());

    // RichText 7: " " (blue)
    RichText rt7 = p.getParagraph().getRichText().get(7);
    assertEquals("text", rt7.getType());
    assertEquals(" ", rt7.getText().getContent());
    assertFalse(rt7.getAnnotations().isBold());
    assertFalse(rt7.getAnnotations().isItalic());
    assertFalse(rt7.getAnnotations().isStrikethrough());
    assertFalse(rt7.getAnnotations().isUnderline());
    assertFalse(rt7.getAnnotations().isCode());
    assertEquals("blue", rt7.getAnnotations().getColor());
    assertEquals(" ", rt7.getPlainText());

    // RichText 8: equation "O(n)" (blue)
    RichText rt8 = p.getParagraph().getRichText().get(8);
    assertEquals("equation", rt8.getType());
    assertNotNull(rt8.getEquation());
    assertEquals("O(n)", rt8.getEquation().getExpression());
    assertFalse(rt8.getAnnotations().isBold());
    assertFalse(rt8.getAnnotations().isItalic());
    assertFalse(rt8.getAnnotations().isStrikethrough());
    assertFalse(rt8.getAnnotations().isUnderline());
    assertFalse(rt8.getAnnotations().isCode());
    assertEquals("blue", rt8.getAnnotations().getColor());
    assertEquals("O(n)", rt8.getPlainText());

    // RichText 9: " " (blue)
    RichText rt9 = p.getParagraph().getRichText().get(9);
    assertEquals("text", rt9.getType());
    assertEquals(" ", rt9.getText().getContent());
    assertFalse(rt9.getAnnotations().isBold());
    assertFalse(rt9.getAnnotations().isItalic());
    assertFalse(rt9.getAnnotations().isStrikethrough());
    assertFalse(rt9.getAnnotations().isUnderline());
    assertFalse(rt9.getAnnotations().isCode());
    assertEquals("blue", rt9.getAnnotations().getColor());
    assertEquals(" ", rt9.getPlainText());

    // RichText 10: "Text" (strikethrough, blue)
    RichText rt10 = p.getParagraph().getRichText().get(10);
    assertEquals("text", rt10.getType());
    assertEquals("Text", rt10.getText().getContent());
    assertFalse(rt10.getAnnotations().isBold());
    assertFalse(rt10.getAnnotations().isItalic());
    assertTrue(rt10.getAnnotations().isStrikethrough());
    assertFalse(rt10.getAnnotations().isUnderline());
    assertFalse(rt10.getAnnotations().isCode());
    assertEquals("blue", rt10.getAnnotations().getColor());
    assertEquals("Text", rt10.getPlainText());
  }

  @Test
  void testParagraphWithUserMention() {
    Block b = findBlockById("22ec5b96-8ec4-806e-a103-e69af307e817");
    assertNotNull(b);

    ParagraphBlock p = b.asParagraph();
    // Block-level
    assertEquals("block", p.getObject());
    assertEquals("22ec5b96-8ec4-806e-a103-e69af307e817", p.getId());
    assertEquals("paragraph", p.getType());
    assertEquals("2025-07-12T17:40:00.000Z", p.getCreatedTime());
    assertEquals("2025-07-12T17:40:00.000Z", p.getLastEditedTime());
    assertFalse(p.getArchived());
    assertFalse(p.getInTrash());
    assertFalse(p.hasChildren());
    assertParent(p, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");

    // Created by
    assertNotNull(p.getCreatedBy());
    assertEquals("user", p.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", p.getCreatedBy().getId());

    // Last edited by
    assertNotNull(p.getLastEditedBy());
    assertEquals("user", p.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", p.getLastEditedBy().getId());

    // Paragraph
    assertNotNull(p.getParagraph());
    assertEquals("default", p.getParagraph().getColor());
    assertNotNull(p.getParagraph().getRichText());
    assertEquals(2, p.getParagraph().getRichText().size());

    // RichText 0: mention
    RichText rt0 = p.getParagraph().getRichText().get(0);
    assertEquals("mention", rt0.getType());
    assertNotNull(rt0.getMention());
    assertEquals("user", rt0.getMention().getType());
    assertNotNull(rt0.getMention().getUser());
    assertEquals("user", rt0.getMention().getUser().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", rt0.getMention().getUser().getId());
    assertEquals("userName", rt0.getMention().getUser().getName());
    assertEquals(
        "https://s3-us-west-2.amazonaws.com/public.notion-static.com/c183efb3-3c41-4cdc-a77b-5802f650e997/avatartion_1_y.png",
        rt0.getMention().getUser().getAvatarUrl());
    assertEquals("person", rt0.getMention().getUser().getType());
    assertNotNull(rt0.getMention().getUser().getPerson());
    assertEquals("user-email@gmail.com", rt0.getMention().getUser().getPerson().getEmail());
    assertNotNull(rt0.getAnnotations());
    assertFalse(rt0.getAnnotations().isBold());
    assertFalse(rt0.getAnnotations().isItalic());
    assertFalse(rt0.getAnnotations().isStrikethrough());
    assertFalse(rt0.getAnnotations().isUnderline());
    assertFalse(rt0.getAnnotations().isCode());
    assertEquals("default", rt0.getAnnotations().getColor());
    assertEquals("@userName", rt0.getPlainText());
    assertNull(rt0.getHref());

    // RichText 1: text
    RichText rt1 = p.getParagraph().getRichText().get(1);
    assertEquals("text", rt1.getType());
    assertNotNull(rt1.getText());
    assertEquals(" ", rt1.getText().getContent());
    assertNull(rt1.getText().getLink());
    assertNotNull(rt1.getAnnotations());
    assertFalse(rt1.getAnnotations().isBold());
    assertFalse(rt1.getAnnotations().isItalic());
    assertFalse(rt1.getAnnotations().isStrikethrough());
    assertFalse(rt1.getAnnotations().isUnderline());
    assertFalse(rt1.getAnnotations().isCode());
    assertEquals("default", rt1.getAnnotations().getColor());
    assertEquals(" ", rt1.getPlainText());
    assertNull(rt1.getHref());
  }

  @Test
  void testParagraphWithPageMention() {
    Block b = findBlockById("22ec5b96-8ec4-80a2-8c79-f01f62618763");
    assertNotNull(b);

    ParagraphBlock p = b.asParagraph();
    assertEquals("block", p.getObject());
    assertEquals("22ec5b96-8ec4-80a2-8c79-f01f62618763", p.getId());
    assertEquals("paragraph", p.getType());
    assertFalse(p.hasChildren());
    assertFalse(p.getArchived());
    assertFalse(p.getInTrash());
    assertParent(p, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-12T17:40:00.000Z", p.getCreatedTime());
    assertEquals("2025-07-12T17:40:00.000Z", p.getLastEditedTime());
    assertNotNull(p.getCreatedBy());
    assertEquals("user", p.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", p.getCreatedBy().getId());
    assertNotNull(p.getLastEditedBy());
    assertEquals("user", p.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", p.getLastEditedBy().getId());
    assertNotNull(p.getParagraph());
    assertEquals("default", p.getParagraph().getColor());
    assertNotNull(p.getParagraph().getRichText());
    assertNull(p.getParagraph().getChildren());
    assertEquals(2, p.getParagraph().getRichText().size());

    // mention
    RichText mention = p.getParagraph().getRichText().get(0);
    assertEquals("mention", mention.getType());
    assertNotNull(mention.getMention());
    assertEquals("page", mention.getMention().getType());
    assertNotNull(mention.getMention().getPage());
    assertEquals("226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8", mention.getMention().getPage().getId());
    assertNotNull(mention.getAnnotations());
    assertFalse(mention.getAnnotations().isBold());
    assertFalse(mention.getAnnotations().isItalic());
    assertFalse(mention.getAnnotations().isStrikethrough());
    assertFalse(mention.getAnnotations().isUnderline());
    assertFalse(mention.getAnnotations().isCode());
    assertEquals("default", mention.getAnnotations().getColor());
    assertEquals("Testing page for Notion SDK (all block types included)", mention.getPlainText());
    assertEquals("https://www.notion.so/226c5b968ec4801fad8ecd6c19d8e0a8", mention.getHref());

    // text
    RichText text = p.getParagraph().getRichText().get(1);
    assertEquals("text", text.getType());
    assertNotNull(text.getText());
    assertEquals(" ", text.getText().getContent());
    assertNull(text.getText().getLink());
    assertNotNull(text.getAnnotations());
    assertFalse(text.getAnnotations().isBold());
    assertFalse(text.getAnnotations().isItalic());
    assertFalse(text.getAnnotations().isStrikethrough());
    assertFalse(text.getAnnotations().isUnderline());
    assertFalse(text.getAnnotations().isCode());
    assertEquals("default", text.getAnnotations().getColor());
    assertEquals(" ", text.getPlainText());
    assertNull(text.getHref());
  }

  @Test
  void testParagraphWithDateMention() {
    Block b = findBlockById("245c5b96-8ec4-80cf-9a24-fe394b47da99");
    assertNotNull(b);
    assertEquals("block", b.getObject());
    assertEquals("245c5b96-8ec4-80cf-9a24-fe394b47da99", b.getId());
    assertEquals("page_id", b.getParent().getType());
    assertEquals("226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8", b.getParent().getPageId());
    assertEquals("2025-08-04T19:55:00.000Z", b.getCreatedTime());
    assertEquals("2025-08-04T19:55:00.000Z", b.getLastEditedTime());
    assertEquals("user", b.getCreatedBy().getObject());
    assertEquals("aaaaaaa2-cccc-eeee-9999-222222222222", b.getCreatedBy().getId());
    assertEquals("user", b.getLastEditedBy().getObject());
    assertEquals("aaaaaaa2-cccc-eeee-9999-222222222222", b.getLastEditedBy().getId());
    assertFalse(b.hasChildren());
    assertFalse(b.getArchived());
    assertFalse(b.getInTrash());

    ParagraphBlock p = b.asParagraph();
    assertEquals("paragraph", p.getType());
    assertNotNull(p.getParagraph());
    assertEquals("default", p.getParagraph().getColor());
    assertNotNull(p.getParagraph().getRichText());
    assertEquals(2, p.getParagraph().getRichText().size());

    RichText mention = p.getParagraph().getRichText().get(0);
    assertEquals("mention", mention.getType());
    assertNotNull(mention.getMention());
    assertEquals("date", mention.getMention().getType());
    assertNotNull(mention.getMention().getDate());
    assertEquals("2025-08-05", mention.getMention().getDate().getStart());
    assertNull(mention.getMention().getDate().getEnd());
    assertNull(mention.getMention().getDate().getTimeZone());
    assertEquals("2025-08-05", mention.getPlainText());

    RichText space = p.getParagraph().getRichText().get(1);
    assertEquals("text", space.getType());
    assertEquals(" ", space.getPlainText());
  }

  @Test
  void testParagraphWithDatabaseMention() {
    Block b = findBlockById("22ec5b96-8ec4-8041-bb35-e4e24ebd8266");
    assertNotNull(b);

    ParagraphBlock p = b.asParagraph();
    assertEquals("block", p.getObject());
    assertEquals("22ec5b96-8ec4-8041-bb35-e4e24ebd8266", p.getId());
    assertEquals("paragraph", p.getType());
    assertFalse(p.hasChildren());
    assertFalse(p.getArchived());
    assertFalse(p.getInTrash());
    assertParent(p, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-12T17:40:00.000Z", p.getCreatedTime());
    assertEquals("2025-07-12T17:41:00.000Z", p.getLastEditedTime());
    assertNotNull(p.getCreatedBy());
    assertEquals("user", p.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", p.getCreatedBy().getId());
    assertNotNull(p.getLastEditedBy());
    assertEquals("user", p.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", p.getLastEditedBy().getId());
    assertNotNull(p.getParagraph());
    assertEquals("default", p.getParagraph().getColor());
    assertNotNull(p.getParagraph().getRichText());
    assertNull(p.getParagraph().getChildren());
    assertEquals(2, p.getParagraph().getRichText().size());

    // mention
    RichText mention = p.getParagraph().getRichText().get(0);
    assertEquals("mention", mention.getType());
    assertNotNull(mention.getMention());
    assertEquals("database", mention.getMention().getType());
    assertNotNull(mention.getMention().getDatabase());
    assertEquals(
        "22ec5b96-8ec4-8013-8ecf-ebc71cfc8536", mention.getMention().getDatabase().getId());
    assertNotNull(mention.getAnnotations());
    assertFalse(mention.getAnnotations().isBold());
    assertFalse(mention.getAnnotations().isItalic());
    assertFalse(mention.getAnnotations().isStrikethrough());
    assertFalse(mention.getAnnotations().isUnderline());
    assertFalse(mention.getAnnotations().isCode());
    assertEquals("default", mention.getAnnotations().getColor());
    assertEquals("/////", mention.getPlainText());
    assertEquals("https://www.notion.so/22ec5b968ec480138ecfebc71cfc8536", mention.getHref());

    // text
    RichText text = p.getParagraph().getRichText().get(1);
    assertEquals("text", text.getType());
    assertNotNull(text.getText());
    assertEquals(" ", text.getText().getContent());
    assertNull(text.getText().getLink());
    assertNotNull(text.getAnnotations());
    assertFalse(text.getAnnotations().isBold());
    assertFalse(text.getAnnotations().isItalic());
    assertFalse(text.getAnnotations().isStrikethrough());
    assertFalse(text.getAnnotations().isUnderline());
    assertFalse(text.getAnnotations().isCode());
    assertEquals("default", text.getAnnotations().getColor());
    assertEquals(" ", text.getPlainText());
    assertNull(text.getHref());
  }

  @Test
  void testParagraphWithLinkMention() {
    Block b = findBlockById("22ec5b96-8ec4-804e-8738-ec9479a42848");
    assertNotNull(b);

    ParagraphBlock p = b.asParagraph();
    assertEquals("block", p.getObject());
    assertEquals("22ec5b96-8ec4-804e-8738-ec9479a42848", p.getId());
    assertEquals("paragraph", p.getType());
    assertFalse(p.hasChildren());
    assertFalse(p.getArchived());
    assertFalse(p.getInTrash());
    assertParent(p, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-12T15:25:00.000Z", p.getCreatedTime());
    assertEquals("2025-07-12T15:27:00.000Z", p.getLastEditedTime());
    assertNotNull(p.getCreatedBy());
    assertEquals("user", p.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", p.getCreatedBy().getId());
    assertNotNull(p.getLastEditedBy());
    assertEquals("user", p.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", p.getLastEditedBy().getId());
    assertNotNull(p.getParagraph());
    assertEquals("default", p.getParagraph().getColor());
    assertNotNull(p.getParagraph().getRichText());
    assertEquals(1, p.getParagraph().getRichText().size());
    RichText mention = p.getParagraph().getRichText().get(0);
    assertEquals("mention", mention.getType());
    assertNotNull(mention.getMention());
    assertEquals("link_mention", mention.getMention().getType());
    assertNotNull(mention.getMention().getLinkMention());
    assertEquals(
        "https://www.airbnb.co.uk/rooms/48823939?check_in=2025-09-12&check_out=2025-09-14&photo_id=1641560102&source_impression_id=p3_1752333984_P3gceXyu0T9HRzYY&previous_page_section_name=1000&_set_bev_on_new_domain=1752333983_EAM2U4MTkxYmVjMT",
        mention.getMention().getLinkMention().getHref());
    assertEquals(
        "Rental unit in Warsaw · ★4.89 · 1 bedroom · 1 bed · 1 shared bathroom",
        mention.getMention().getLinkMention().getTitle());
    assertEquals(
        "https://a0.muscache.com/im/pictures/airbnb-platform-assets/AirbnbPlatformAssets-Favicons/original/0d189acb-3f82-4b2c-b95f-ad1d6a803d13.png?im_w=240",
        mention.getMention().getLinkMention().getIconUrl());
    assertEquals(
        "Great deal! Cheap, nice room in the center of Warsaw",
        mention.getMention().getLinkMention().getDescription());
    assertEquals("Airbnb", mention.getMention().getLinkMention().getLinkProvider());
    assertEquals(
        "https://a0.muscache.com/im/pictures/airflow/Hosting-48823939/original/f4b99652-c01b-4320-a446-f2e264b5b838.jpg?im_w=720&width=720&quality=70&auto=webp",
        mention.getMention().getLinkMention().getThumbnailUrl());
    assertNotNull(mention.getAnnotations());
    assertFalse(mention.getAnnotations().isBold());
    assertFalse(mention.getAnnotations().isItalic());
    assertFalse(mention.getAnnotations().isUnderline());
    assertFalse(mention.getAnnotations().isCode());
    assertFalse(mention.getAnnotations().isStrikethrough());
    assertEquals("default", mention.getAnnotations().getColor());
    assertEquals(
        "https://www.airbnb.co.uk/rooms/48823939?check_in=2025-09-12&check_out=2025-09-14&photo_id=1641560102&source_impression_id=p3_1752333984_P3gceXyu0T9HRzYY&previous_page_section_name=1000&_set_bev_on_new_domain=1752333983_EAM2U4MTkxYmVjMT",
        mention.getPlainText());
    assertEquals(
        "https://www.airbnb.co.uk/rooms/48823939?check_in=2025-09-12&check_out=2025-09-14&photo_id=1641560102&source_impression_id=p3_1752333984_P3gceXyu0T9HRzYY&previous_page_section_name=1000&_set_bev_on_new_domain=1752333983_EAM2U4MTkxYmVjMT",
        mention.getHref());
  }
}
