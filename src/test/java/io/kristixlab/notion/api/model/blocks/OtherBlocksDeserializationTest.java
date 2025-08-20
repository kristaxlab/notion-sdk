package io.kristixlab.notion.api.model.blocks;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.common.RichText;
import org.junit.jupiter.api.Test;

class OtherBlocksDeserializationTest extends BaseBlockDeserializationTest {

  @Test
  void testBookmark() {
    Block b = findBlockById("22dc5b96-8ec4-80e4-b4d4-d87dfa404035");
    assertNotNull(b);

    assertEquals("block", b.getObject());
    assertEquals("22dc5b96-8ec4-80e4-b4d4-d87dfa404035", b.getId());
    assertEquals("page_id", b.getParent().getType());
    assertEquals("226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8", b.getParent().getPageId());
    assertEquals("2025-07-11T18:09:00.000Z", b.getCreatedTime());
    assertEquals("2025-07-11T18:09:00.000Z", b.getLastEditedTime());
    assertEquals("user", b.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", b.getCreatedBy().getId());
    assertEquals("user", b.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", b.getLastEditedBy().getId());
    assertFalse(b.hasChildren());
    assertFalse(b.getArchived());
    assertFalse(b.getInTrash());

    BookmarkBlock block = b.asBookmark();
    assertEquals("bookmark", block.getType());
    assertNotNull(block.getBookmark());
    assertEquals(0, block.getBookmark().getCaption().size());
    assertEquals("https://boosty.to/notion", block.getBookmark().getUrl());
  }

  @Test
  void testBreadcrumb() {
    Block block = findBlockById("22ec5b96-8ec4-80fd-b126-df21eb33b7b3");
    assertNotNull(block);

    BreadcrumbBlock b = block.asBreadcrumb();
    assertEquals("block", b.getObject());
    assertEquals("22ec5b96-8ec4-80fd-b126-df21eb33b7b3", b.getId());
    assertEquals("breadcrumb", b.getType());
    assertFalse(b.hasChildren());
    assertFalse(b.getArchived());
    assertFalse(b.getInTrash());
    assertParent(b, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-12T15:25:00.000Z", b.getCreatedTime());
    assertEquals("2025-07-12T15:25:00.000Z", b.getLastEditedTime());
    assertNotNull(b.getCreatedBy());
    assertEquals("user", b.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", b.getCreatedBy().getId());
    assertNotNull(b.getLastEditedBy());
    assertEquals("user", b.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", b.getLastEditedBy().getId());
    assertNotNull(b.getBreadcrumb());
    // breadcrumb is an empty object, nothing else to check

  }

  @Test
  void testCallout() {
    Block b = findBlockById("22dc5b96-8ec4-8067-884f-c9f32976b29c");
    assertNotNull(b);

    CalloutBlock c = b.asCallout();
    assertEquals("block", c.getObject());
    assertEquals("callout", c.getType());
    assertFalse(c.hasChildren());
    assertFalse(c.getArchived());
    assertFalse(c.getInTrash());
    assertParent(c, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-11T17:59:00.000Z", c.getCreatedTime());
    assertEquals("2025-07-11T17:59:00.000Z", c.getLastEditedTime());
    assertNotNull(c.getCreatedBy());
    assertEquals("user", c.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", c.getCreatedBy().getId());
    assertNotNull(c.getLastEditedBy());
    assertEquals("user", c.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", c.getLastEditedBy().getId());
    assertNotNull(c.getCallout());
    assertEquals("blue_background", c.getCallout().getColor());
    assertNull(c.getCallout().getIcon());
    assertNotNull(c.getCallout().getRichText());
    assertEquals(1, c.getCallout().getRichText().size());
    RichText text = c.getCallout().getRichText().get(0);
    assertEquals("text", text.getType());
    assertNotNull(text.getText());
    assertEquals("Callout", text.getText().getContent());
    assertNull(text.getText().getLink());
    assertNotNull(text.getAnnotations());
    assertFalse(text.getAnnotations().isBold());
    assertFalse(text.getAnnotations().isItalic());
    assertFalse(text.getAnnotations().isStrikethrough());
    assertFalse(text.getAnnotations().isUnderline());
    assertFalse(text.getAnnotations().isCode());
    assertEquals("default", text.getAnnotations().getColor());
    assertEquals("Callout", text.getPlainText());
    assertNull(text.getHref());
  }

  @Test
  void testCalloutWithIcon() {
    Block b = findBlockById("22dc5b96-8ec4-8029-8306-d57152713f50");
    assertNotNull(b);

    CalloutBlock c = b.asCallout();
    assertEquals("block", c.getObject());
    assertEquals("callout", c.getType());
    assertFalse(c.hasChildren());
    assertFalse(c.getArchived());
    assertFalse(c.getInTrash());
    assertParent(c, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-11T17:59:00.000Z", c.getCreatedTime());
    assertEquals("2025-07-11T17:59:00.000Z", c.getLastEditedTime());
    assertNotNull(c.getCreatedBy());
    assertEquals("user", c.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", c.getCreatedBy().getId());
    assertNotNull(c.getLastEditedBy());
    assertEquals("user", c.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", c.getLastEditedBy().getId());
    assertNotNull(c.getCallout());
    assertEquals("blue_background", c.getCallout().getColor());
    assertNotNull(c.getCallout().getIcon());
    assertEquals("external", c.getCallout().getIcon().getType());
    assertNotNull(c.getCallout().getIcon().getExternal());
    assertEquals(
        "https://www.notion.so/icons/command-line_purple.svg",
        c.getCallout().getIcon().getExternal().getUrl());
    assertNotNull(c.getCallout().getRichText());
    assertEquals(1, c.getCallout().getRichText().size());
    RichText text = c.getCallout().getRichText().get(0);
    assertEquals("text", text.getType());
    assertNotNull(text.getText());
    assertEquals("Another callout wiht icon", text.getText().getContent());
    assertNull(text.getText().getLink());
    assertNotNull(text.getAnnotations());
    assertFalse(text.getAnnotations().isBold());
    assertFalse(text.getAnnotations().isItalic());
    assertFalse(text.getAnnotations().isStrikethrough());
    assertFalse(text.getAnnotations().isUnderline());
    assertFalse(text.getAnnotations().isCode());
    assertEquals("default", text.getAnnotations().getColor());
    assertEquals("Another callout wiht icon", text.getPlainText());
    assertNull(text.getHref());
  }

  @Test
  void testChildDatabase() {
    Block b = findBlockById("22ec5b96-8ec4-8013-8ecf-ebc71cfc8536");
    assertNotNull(b);

    ChildDatabaseBlock db = b.asChildDatabase();
    assertEquals("block", db.getObject());
    assertEquals("22ec5b96-8ec4-8013-8ecf-ebc71cfc8536", db.getId());
    assertEquals("child_database", db.getType());
    assertFalse(db.hasChildren());
    assertFalse(db.getArchived());
    assertFalse(db.getInTrash());
    assertParent(db, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-12T17:41:00.000Z", db.getCreatedTime());
    assertEquals("2025-07-12T17:41:00.000Z", db.getLastEditedTime());
    assertNotNull(db.getCreatedBy());
    assertEquals("user", db.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", db.getCreatedBy().getId());
    assertNotNull(db.getLastEditedBy());
    assertEquals("user", db.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", db.getLastEditedBy().getId());
    assertNotNull(db.getChildDatabase());
    assertEquals("/////", db.getChildDatabase().getTitle());
  }

  @Test
  void testChildPage() {
    Block b = findBlockById("22dc5b96-8ec4-801f-a37e-d931a6ac7e37");
    assertNotNull(b);

    ChildPageBlock c = b.asChildPage();
    assertEquals("block", c.getObject());
    assertEquals("child_page", c.getType());
    assertTrue(c.hasChildren());
    assertFalse(c.getArchived());
    assertFalse(c.getInTrash());
    assertParent(c, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-11T18:04:00.000Z", c.getCreatedTime());
    assertEquals("2025-07-11T18:04:00.000Z", c.getLastEditedTime());
    assertNotNull(c.getCreatedBy());
    assertEquals("user", c.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", c.getCreatedBy().getId());
    assertNotNull(c.getLastEditedBy());
    assertEquals("user", c.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", c.getLastEditedBy().getId());
    assertNotNull(c.getChildPage());
    assertEquals("Nested page", c.getChildPage().getTitle());
  }

  @Test
  void testCode() {
    Block b = findBlockById("22dc5b96-8ec4-805b-a19a-ce829341197e");
    assertNotNull(b);

    CodeBlock c = b.asCode();
    assertEquals("block", c.getObject());
    assertEquals("code", c.getType());
    assertFalse(c.hasChildren());
    assertFalse(c.getArchived());
    assertFalse(c.getInTrash());
    assertParent(c, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-11T18:04:00.000Z", c.getCreatedTime());
    assertEquals("2025-07-11T18:10:00.000Z", c.getLastEditedTime());
    assertNotNull(c.getCreatedBy());
    assertEquals("user", c.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", c.getCreatedBy().getId());
    assertNotNull(c.getLastEditedBy());
    assertEquals("user", c.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", c.getLastEditedBy().getId());
    assertNotNull(c.getCode());
    assertEquals("java", c.getCode().getLanguage());
    assertNotNull(c.getCode().getRichText());
    assertEquals(1, c.getCode().getRichText().size());
    RichText text = c.getCode().getRichText().get(0);
    assertEquals("text", text.getType());
    assertNotNull(text.getText());
    assertEquals("public class JavaCalss {\n\n}", text.getText().getContent());
    assertNull(text.getText().getLink());
    assertEquals("public class JavaCalss {\n\n}", text.getPlainText());
    assertNull(text.getHref());
    assertNotNull(text.getAnnotations());
    assertFalse(text.getAnnotations().isBold());
    assertFalse(text.getAnnotations().isItalic());
    assertFalse(text.getAnnotations().isStrikethrough());
    assertFalse(text.getAnnotations().isUnderline());
    assertFalse(text.getAnnotations().isCode());
    assertEquals("default", text.getAnnotations().getColor());
    assertNotNull(c.getCode().getCaption());
    assertEquals(0, c.getCode().getCaption().size());
  }

  @Test
  void testColumnList() {
    Block b = findBlockById("22ec5b96-8ec4-80c0-951d-caa5a1a76f0c");
    assertNotNull(b);

    ColumnListBlock c = b.asColumnList();
    assertEquals("block", c.getObject());
    assertEquals("22ec5b96-8ec4-80c0-951d-caa5a1a76f0c", c.getId());
    assertEquals("column_list", c.getType());
    assertTrue(c.hasChildren());
    assertFalse(c.getArchived());
    assertFalse(c.getInTrash());
    assertParent(c, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-12T15:25:00.000Z", c.getCreatedTime());
    assertEquals("2025-07-12T15:25:00.000Z", c.getLastEditedTime());
    assertNotNull(c.getCreatedBy());
    assertEquals("user", c.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", c.getCreatedBy().getId());
    assertNotNull(c.getLastEditedBy());
    assertEquals("user", c.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", c.getLastEditedBy().getId());
  }

  @Test
  void testDivider() {
    Block b = findBlockById("22dc5b96-8ec4-80ed-8cda-df443141b1c9");
    assertNotNull(b);

    DividerBlock d = b.asDivider();
    assertEquals("block", d.getObject());
    assertEquals("divider", d.getType());
    assertFalse(d.hasChildren());
    assertFalse(d.getArchived());
    assertFalse(d.getInTrash());
    assertParent(d, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-11T18:05:00.000Z", d.getCreatedTime());
    assertEquals("2025-07-11T18:05:00.000Z", d.getLastEditedTime());
    assertNotNull(d.getCreatedBy());
    assertEquals("user", d.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", d.getCreatedBy().getId());
    assertNotNull(d.getLastEditedBy());
    assertEquals("user", d.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", d.getLastEditedBy().getId());
    assertNotNull(d.getDivider());
  }

  @Test
  void testLinkPreview() {
    Block b = findBlockById("245c5b96-8ec4-8022-9458-e72565ed7f88");
    assertNotNull(b);
    assertEquals("block", b.getObject());
    assertEquals("245c5b96-8ec4-8022-9458-e72565ed7f88", b.getId());
    assertEquals("page_id", b.getParent().getType());
    assertEquals("226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8", b.getParent().getPageId());
    assertEquals("2025-08-04T19:18:00.000Z", b.getCreatedTime());
    assertEquals("2025-08-04T19:19:00.000Z", b.getLastEditedTime());
    assertEquals("user", b.getCreatedBy().getObject());
    assertEquals("aaaaaaa2-cccc-eeee-9999-222222222222", b.getCreatedBy().getId());
    assertEquals("user", b.getLastEditedBy().getObject());
    assertEquals("0afd2e35-37fb-4214-96f1-92fadbd8fc69", b.getLastEditedBy().getId());
    assertFalse(b.hasChildren());
    assertFalse(b.getArchived());
    assertFalse(b.getInTrash());

    assertEquals("link_preview", b.getType());
    LinkPreviewBlock block = b.asLinkPreview();
    assertNotNull(block.getLinkPreview());
    assertEquals(
        "https://drive.google.com/file/d/1FFcfQqw2OWj2l5zyBSUf5caorWRGvYcO/view?usp=drive_link",
        block.getLinkPreview().getUrl());
  }

  @Test
  void testLinkToPage() {
    Block b = findBlockById("22dc5b96-8ec4-80fa-affd-e4ee03f96d1b");
    assertNotNull(b);

    LinkToPageBlock l = b.asLinkToPage();
    assertEquals("block", l.getObject());
    assertEquals("link_to_page", l.getType());
    assertFalse(l.hasChildren());
    assertFalse(l.getArchived());
    assertFalse(l.getInTrash());
    assertParent(l, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-11T18:08:00.000Z", l.getCreatedTime());
    assertEquals("2025-07-11T18:10:00.000Z", l.getLastEditedTime());
    assertNotNull(l.getCreatedBy());
    assertEquals("user", l.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", l.getCreatedBy().getId());
    assertNotNull(l.getLastEditedBy());
    assertEquals("user", l.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", l.getLastEditedBy().getId());
    assertNotNull(l.getLinkToPage());
    assertEquals("page_id", l.getLinkToPage().getType());
    assertEquals("22dc5b96-8ec4-801f-a37e-d931a6ac7e37", l.getLinkToPage().getPageId());
  }

  @Test
  void testQuote() {
    Block b = findBlockById("22dc5b96-8ec4-80d1-8d72-e6585b7e8525");
    assertNotNull(b);

    QuoteBlock q = b.asQuote();
    assertEquals("block", q.getObject());
    assertEquals("quote", q.getType());
    assertFalse(q.hasChildren());
    assertFalse(q.getArchived());
    assertFalse(q.getInTrash());
    assertParent(q, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-11T18:04:00.000Z", q.getCreatedTime());
    assertEquals("2025-07-11T18:04:00.000Z", q.getLastEditedTime());
    assertNotNull(q.getCreatedBy());
    assertEquals("user", q.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", q.getCreatedBy().getId());
    assertNotNull(q.getLastEditedBy());
    assertEquals("user", q.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", q.getLastEditedBy().getId());
    assertNotNull(q.getQuote());
    assertEquals("default", q.getQuote().getColor());
    assertNotNull(q.getQuote().getRichText());
    assertEquals(1, q.getQuote().getRichText().size());
    RichText text = q.getQuote().getRichText().get(0);
    assertEquals("text", text.getType());
    assertNotNull(text.getText());
    assertEquals("My best quote is here", text.getText().getContent());
    assertNull(text.getText().getLink());
    assertEquals("My best quote is here", text.getPlainText());
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
  void testSyncedBlock() {
    Block b = findBlockById("22ec5b96-8ec4-806a-a034-eeba15e6d53f");
    assertNotNull(b);

    SyncedBlock s = b.asSynced();
    assertEquals("block", s.getObject());
    assertEquals("22ec5b96-8ec4-806a-a034-eeba15e6d53f", s.getId());
    assertEquals("synced_block", s.getType());
    assertTrue(s.hasChildren());
    assertFalse(s.getArchived());
    assertFalse(s.getInTrash());
    assertParent(s, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-12T15:25:00.000Z", s.getCreatedTime());
    assertEquals("2025-07-12T15:25:00.000Z", s.getLastEditedTime());
    assertNotNull(s.getCreatedBy());
    assertEquals("user", s.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", s.getCreatedBy().getId());
    assertNotNull(s.getLastEditedBy());
    assertEquals("user", s.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", s.getLastEditedBy().getId());
    assertNotNull(s.getSyncedBlock());
    assertNull(s.getSyncedBlock().getSyncedFrom());
  }

  @Test
  void testTable() {
    Block b = findBlockById("22dc5b96-8ec4-8064-a249-d3c338e9e1d6");
    assertNotNull(b);

    TableBlock t = b.asTable();
    assertEquals("block", t.getObject());
    assertEquals("table", t.getType());
    assertTrue(t.hasChildren());
    assertFalse(t.getArchived());
    assertFalse(t.getInTrash());
    assertParent(t, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-11T18:05:00.000Z", t.getCreatedTime());
    assertEquals("2025-07-11T18:05:00.000Z", t.getLastEditedTime());
    assertNotNull(t.getCreatedBy());
    assertEquals("user", t.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", t.getCreatedBy().getId());
    assertNotNull(t.getLastEditedBy());
    assertEquals("user", t.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", t.getLastEditedBy().getId());
    assertNotNull(t.getTable());
    assertEquals(2, t.getTable().getTableWidth());
    assertTrue(t.getTable().hasColumnHeader());
    assertTrue(t.getTable().hasRowHeader());
  }

  @Test
  void testTableOfContent() {
    Block b = findBlockById("247c5b96-8ec4-80c7-8e3d-f9d7dda7fd50");
    assertNotNull(b);

    TableOfContentsBlock t = b.asTableOfContents();
    assertEquals("block", t.getObject());
    assertEquals("table_of_contents", t.getType());
    assertNotNull(t.getTableOfContents());
    assertNotNull(t.getTableOfContents().getColor());
    assertEquals("gray", t.getTableOfContents().getColor());
  }


  @Test
  void testUnknown() {
    Block block = findBlockById("11111111-8ec4-8013-8ecf-ebc71cfc1111");
    assertNotNull(block);

    UnknownBlock b = block.asUnknown();
    assertEquals("block", b.getObject());
    assertEquals("11111111-8ec4-8013-8ecf-ebc71cfc1111", b.getId());
    assertEquals("some_new_type", b.getType());
    assertFalse(b.hasChildren());
    assertFalse(b.getArchived());
    assertFalse(b.getInTrash());
    assertParent(b, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-12T17:41:00.000Z", b.getCreatedTime());
    assertEquals("2025-07-12T17:41:00.000Z", b.getLastEditedTime());
    assertNotNull(b.getCreatedBy());
    assertEquals("user", b.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", b.getCreatedBy().getId());
    assertNotNull(b.getLastEditedBy());
    assertEquals("user", b.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", b.getLastEditedBy().getId());
  }

  @Test
  void testUnsupported() {
    Block b = findBlockById("22dc5b96-8ec4-8054-813c-c5bfcd5c4901");
    assertNotNull(b);

    UnsupportedBlock u = b.asUnsupported();
    assertEquals("block", u.getObject());
    assertEquals("unsupported", u.getType());
    assertFalse(u.hasChildren());
    assertFalse(u.getArchived());
    assertFalse(u.getInTrash());
    assertParent(u, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
    assertEquals("2025-07-11T18:08:00.000Z", u.getCreatedTime());
    assertEquals("2025-07-11T18:10:00.000Z", u.getLastEditedTime());
    assertNotNull(u.getCreatedBy());
    assertEquals("user", u.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", u.getCreatedBy().getId());
    assertNotNull(u.getLastEditedBy());
    assertEquals("user", u.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", u.getLastEditedBy().getId());
    assertNotNull(u.getUnsupported());
  }
}
