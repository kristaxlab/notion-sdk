package io.kristixlab.notion.api.model.helper;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.block.*;
import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MarkdownToBlocksTest {

  @Test
  void ofNullThrowsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> MarkdownToBlocks.of(null));
  }

  @Test
  void ofEmptyStringReturnsEmptyList() {
    List<Block> blocks = MarkdownToBlocks.of("");
    assertTrue(blocks.isEmpty());
  }

  @Test
  void ofBlankLinesOnlyReturnsEmptyList() {
    List<Block> blocks = MarkdownToBlocks.of("\n\n\n");
    assertTrue(blocks.isEmpty());
  }

  @Nested
  class Headings {

    @Test
    void heading1() {
      List<Block> blocks = MarkdownToBlocks.of("# Hello World");
      assertEquals(1, blocks.size());
      assertInstanceOf(HeadingOneBlock.class, blocks.get(0));
      HeadingOneBlock h = (HeadingOneBlock) blocks.get(0);
      assertEquals("Hello World", h.getHeading1().getRichText().get(0).getPlainText());
    }

    @Test
    void heading2() {
      List<Block> blocks = MarkdownToBlocks.of("## Sub-heading");
      assertEquals(1, blocks.size());
      assertInstanceOf(HeadingTwoBlock.class, blocks.get(0));
      HeadingTwoBlock h = (HeadingTwoBlock) blocks.get(0);
      assertEquals("Sub-heading", h.getHeading2().getRichText().get(0).getPlainText());
    }

    @Test
    void heading3() {
      List<Block> blocks = MarkdownToBlocks.of("### Third level");
      assertEquals(1, blocks.size());
      assertInstanceOf(HeadingThreeBlock.class, blocks.get(0));
    }

    @Test
    void heading4() {
      List<Block> blocks = MarkdownToBlocks.of("#### Fourth level");
      assertEquals(1, blocks.size());
      assertInstanceOf(HeadingFourBlock.class, blocks.get(0));
    }

    @Test
    void headingWithInlineFormatting() {
      List<Block> blocks = MarkdownToBlocks.of("# Hello **bold** world");
      assertEquals(1, blocks.size());
      HeadingOneBlock h = (HeadingOneBlock) blocks.get(0);
      List<RichText> richText = h.getHeading1().getRichText();
      assertTrue(richText.size() >= 2);
      // The bold segment should have bold annotation
      boolean hasBold =
          richText.stream()
              .anyMatch(
                  rt ->
                      "bold".equals(rt.getPlainText())
                          && Boolean.TRUE.equals(rt.getAnnotations().getBold()));
      assertTrue(hasBold);
    }
  }

  @Nested
  class Paragraphs {

    @Test
    void simpleParagraph() {
      List<Block> blocks = MarkdownToBlocks.of("This is a paragraph.");
      assertEquals(1, blocks.size());
      assertInstanceOf(ParagraphBlock.class, blocks.get(0));
      ParagraphBlock p = (ParagraphBlock) blocks.get(0);
      assertEquals("This is a paragraph.", p.getParagraph().getRichText().get(0).getPlainText());
    }

    @Test
    void multipleParagraphsSeparatedByBlankLines() {
      List<Block> blocks = MarkdownToBlocks.of("First paragraph.\n\nSecond paragraph.");
      assertEquals(2, blocks.size());
      assertInstanceOf(ParagraphBlock.class, blocks.get(0));
      assertInstanceOf(ParagraphBlock.class, blocks.get(1));
    }
  }

  @Nested
  class BulletedLists {

    @Test
    void dashBulletList() {
      List<Block> blocks = MarkdownToBlocks.of("- Item 1\n- Item 2\n- Item 3");
      assertEquals(3, blocks.size());
      for (Block block : blocks) {
        assertInstanceOf(BulletedListItemBlock.class, block);
      }
      BulletedListItemBlock first = (BulletedListItemBlock) blocks.get(0);
      assertEquals("Item 1", first.getBulletedListItem().getRichText().get(0).getPlainText());
    }

    @Test
    void asteriskBulletList() {
      List<Block> blocks = MarkdownToBlocks.of("* Alpha\n* Beta");
      assertEquals(2, blocks.size());
      for (Block block : blocks) {
        assertInstanceOf(BulletedListItemBlock.class, block);
      }
    }
  }

  @Nested
  class NumberedLists {

    @Test
    void numberedList() {
      List<Block> blocks = MarkdownToBlocks.of("1. First\n2. Second\n3. Third");
      assertEquals(3, blocks.size());
      for (Block block : blocks) {
        assertInstanceOf(NumberedListItemBlock.class, block);
      }
      NumberedListItemBlock first = (NumberedListItemBlock) blocks.get(0);
      assertEquals("First", first.getNumberedListItem().getRichText().get(0).getPlainText());
    }
  }

  @Nested
  class TodoItems {

    @Test
    void uncheckedTodo() {
      List<Block> blocks = MarkdownToBlocks.of("- [ ] Buy groceries");
      assertEquals(1, blocks.size());
      assertInstanceOf(ToDoBlock.class, blocks.get(0));
      ToDoBlock todo = (ToDoBlock) blocks.get(0);
      assertEquals("Buy groceries", todo.getToDo().getRichText().get(0).getPlainText());
      assertFalse(todo.getToDo().getChecked());
    }

    @Test
    void checkedTodo() {
      List<Block> blocks = MarkdownToBlocks.of("- [x] Done task");
      assertEquals(1, blocks.size());
      assertInstanceOf(ToDoBlock.class, blocks.get(0));
      ToDoBlock todo = (ToDoBlock) blocks.get(0);
      assertTrue(todo.getToDo().getChecked());
    }

    @Test
    void checkedTodoUpperCase() {
      List<Block> blocks = MarkdownToBlocks.of("- [X] Done task");
      assertEquals(1, blocks.size());
      assertInstanceOf(ToDoBlock.class, blocks.get(0));
      ToDoBlock todo = (ToDoBlock) blocks.get(0);
      assertTrue(todo.getToDo().getChecked());
    }

    @Test
    void mixedTodos() {
      List<Block> blocks = MarkdownToBlocks.of("- [ ] Unchecked\n- [x] Checked");
      assertEquals(2, blocks.size());
      assertFalse(((ToDoBlock) blocks.get(0)).getToDo().getChecked());
      assertTrue(((ToDoBlock) blocks.get(1)).getToDo().getChecked());
    }
  }

  @Nested
  class CodeBlocks {

    @Test
    void codeBlockWithLanguage() {
      List<Block> blocks = MarkdownToBlocks.of("```java\npublic class Foo {}\n```");
      assertEquals(1, blocks.size());
      assertInstanceOf(CodeBlock.class, blocks.get(0));
      CodeBlock code = (CodeBlock) blocks.get(0);
      assertEquals("java", code.getCode().getLanguage());
      assertEquals("public class Foo {}", code.getCode().getRichText().get(0).getPlainText());
    }

    @Test
    void codeBlockWithoutLanguage() {
      List<Block> blocks = MarkdownToBlocks.of("```\nhello world\n```");
      assertEquals(1, blocks.size());
      assertInstanceOf(CodeBlock.class, blocks.get(0));
      CodeBlock code = (CodeBlock) blocks.get(0);
      assertNull(code.getCode().getLanguage());
    }

    @Test
    void codeBlockMultipleLines() {
      String md = "```python\ndef hello():\n    print('hi')\n```";
      List<Block> blocks = MarkdownToBlocks.of(md);
      assertEquals(1, blocks.size());
      CodeBlock code = (CodeBlock) blocks.get(0);
      assertEquals("python", code.getCode().getLanguage());
      assertEquals(
          "def hello():\n    print('hi')", code.getCode().getRichText().get(0).getPlainText());
    }

    @Test
    void codeBlockUnclosed() {
      List<Block> blocks = MarkdownToBlocks.of("```java\nsome code");
      assertEquals(1, blocks.size());
      assertInstanceOf(CodeBlock.class, blocks.get(0));
      CodeBlock code = (CodeBlock) blocks.get(0);
      assertEquals("some code", code.getCode().getRichText().get(0).getPlainText());
    }
  }

  @Nested
  class Blockquotes {

    @Test
    void simpleBlockquote() {
      List<Block> blocks = MarkdownToBlocks.of("> This is a quote");
      assertEquals(1, blocks.size());
      assertInstanceOf(QuoteBlock.class, blocks.get(0));
      QuoteBlock quote = (QuoteBlock) blocks.get(0);
      assertEquals("This is a quote", quote.getQuote().getRichText().get(0).getPlainText());
    }

    @Test
    void blockquoteWithoutSpace() {
      List<Block> blocks = MarkdownToBlocks.of(">No space");
      assertEquals(1, blocks.size());
      assertInstanceOf(QuoteBlock.class, blocks.get(0));
      QuoteBlock quote = (QuoteBlock) blocks.get(0);
      assertEquals("No space", quote.getQuote().getRichText().get(0).getPlainText());
    }
  }

  @Nested
  class Dividers {

    @Test
    void dashDivider() {
      List<Block> blocks = MarkdownToBlocks.of("---");
      assertEquals(1, blocks.size());
      assertInstanceOf(DividerBlock.class, blocks.get(0));
    }

    @Test
    void asteriskDivider() {
      List<Block> blocks = MarkdownToBlocks.of("***");
      assertEquals(1, blocks.size());
      assertInstanceOf(DividerBlock.class, blocks.get(0));
    }

    @Test
    void underscoreDivider() {
      List<Block> blocks = MarkdownToBlocks.of("___");
      assertEquals(1, blocks.size());
      assertInstanceOf(DividerBlock.class, blocks.get(0));
    }
  }

  @Nested
  class Images {

    @Test
    void imageBlock() {
      List<Block> blocks = MarkdownToBlocks.of("![Alt text](https://example.com/image.png)");
      assertEquals(1, blocks.size());
      assertInstanceOf(ImageBlock.class, blocks.get(0));
    }
  }

  @Nested
  class InlineFormatting {

    @Test
    void boldText() {
      List<RichText> result = MarkdownToBlocks.parseInlineFormatting("Hello **bold** world");
      assertTrue(result.size() >= 2);
      boolean hasBold =
          result.stream()
              .anyMatch(
                  rt ->
                      "bold".equals(rt.getPlainText())
                          && Boolean.TRUE.equals(rt.getAnnotations().getBold()));
      assertTrue(hasBold);
    }

    @Test
    void italicWithAsterisk() {
      List<RichText> result = MarkdownToBlocks.parseInlineFormatting("Hello *italic* world");
      boolean hasItalic =
          result.stream()
              .anyMatch(
                  rt ->
                      "italic".equals(rt.getPlainText())
                          && Boolean.TRUE.equals(rt.getAnnotations().getItalic()));
      assertTrue(hasItalic);
    }

    @Test
    void italicWithUnderscore() {
      List<RichText> result = MarkdownToBlocks.parseInlineFormatting("Hello _italic_ world");
      boolean hasItalic =
          result.stream()
              .anyMatch(
                  rt ->
                      "italic".equals(rt.getPlainText())
                          && Boolean.TRUE.equals(rt.getAnnotations().getItalic()));
      assertTrue(hasItalic);
    }

    @Test
    void strikethroughText() {
      List<RichText> result = MarkdownToBlocks.parseInlineFormatting("Hello ~~removed~~ world");
      boolean hasStrike =
          result.stream()
              .anyMatch(
                  rt ->
                      "removed".equals(rt.getPlainText())
                          && Boolean.TRUE.equals(rt.getAnnotations().getStrikethrough()));
      assertTrue(hasStrike);
    }

    @Test
    void inlineCode() {
      List<RichText> result = MarkdownToBlocks.parseInlineFormatting("Use `System.out` here");
      boolean hasCode =
          result.stream()
              .anyMatch(
                  rt ->
                      "System.out".equals(rt.getPlainText())
                          && Boolean.TRUE.equals(rt.getAnnotations().getCode()));
      assertTrue(hasCode);
    }

    @Test
    void linkText() {
      List<RichText> result =
          MarkdownToBlocks.parseInlineFormatting("Visit [Google](https://google.com) now");
      boolean hasLink =
          result.stream()
              .anyMatch(
                  rt ->
                      "Google".equals(rt.getPlainText())
                          && "https://google.com".equals(rt.getHref()));
      assertTrue(hasLink);
    }

    @Test
    void plainTextOnly() {
      List<RichText> result = MarkdownToBlocks.parseInlineFormatting("No formatting here");
      assertEquals(1, result.size());
      assertEquals("No formatting here", result.get(0).getPlainText());
      assertNull(result.get(0).getAnnotations().getBold());
    }

    @Test
    void emptyText() {
      List<RichText> result = MarkdownToBlocks.parseInlineFormatting("");
      assertEquals(1, result.size());
      assertEquals("", result.get(0).getPlainText());
    }

    @Test
    void nullText() {
      List<RichText> result = MarkdownToBlocks.parseInlineFormatting(null);
      assertEquals(1, result.size());
    }
  }

  @Nested
  class MixedContent {

    @Test
    void headingFollowedByParagraphAndList() {
      String md = "# Title\n\nSome text.\n\n- Item A\n- Item B";
      List<Block> blocks = MarkdownToBlocks.of(md);
      assertEquals(4, blocks.size());
      assertInstanceOf(HeadingOneBlock.class, blocks.get(0));
      assertInstanceOf(ParagraphBlock.class, blocks.get(1));
      assertInstanceOf(BulletedListItemBlock.class, blocks.get(2));
      assertInstanceOf(BulletedListItemBlock.class, blocks.get(3));
    }

    @Test
    void fullDocument() {
      String md =
          String.join(
              "\n",
              "# Project Overview",
              "",
              "This is the introduction.",
              "",
              "## Features",
              "",
              "- Feature one",
              "- Feature two",
              "",
              "### Code Example",
              "",
              "```java",
              "System.out.println(\"Hello\");",
              "```",
              "",
              "> Important note",
              "",
              "---",
              "",
              "1. Step one",
              "2. Step two",
              "",
              "- [ ] Task pending",
              "- [x] Task done");
      List<Block> blocks = MarkdownToBlocks.of(md);

      assertInstanceOf(HeadingOneBlock.class, blocks.get(0));
      assertInstanceOf(ParagraphBlock.class, blocks.get(1));
      assertInstanceOf(HeadingTwoBlock.class, blocks.get(2));
      assertInstanceOf(BulletedListItemBlock.class, blocks.get(3));
      assertInstanceOf(BulletedListItemBlock.class, blocks.get(4));
      assertInstanceOf(HeadingThreeBlock.class, blocks.get(5));
      assertInstanceOf(CodeBlock.class, blocks.get(6));
      assertInstanceOf(QuoteBlock.class, blocks.get(7));
      assertInstanceOf(DividerBlock.class, blocks.get(8));
      assertInstanceOf(NumberedListItemBlock.class, blocks.get(9));
      assertInstanceOf(NumberedListItemBlock.class, blocks.get(10));
      assertInstanceOf(ToDoBlock.class, blocks.get(11));
      assertInstanceOf(ToDoBlock.class, blocks.get(12));
      assertEquals(13, blocks.size());
    }
  }
}
