package io.kristixlab.notion.api.model.helper;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.block.*;
import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class BlockListViewTest {

  @Nested
  class Factories {

    @Test
    void ofNullList_returnsEmptyView() {
      BlockListView view = BlockListView.of((List<Block>) null);
      assertTrue(view.isEmpty());
      assertEquals(0, view.size());
    }

    @Test
    void ofEmptyList_returnsEmptyView() {
      BlockListView view = BlockListView.of(new ArrayList<>());
      assertTrue(view.isEmpty());
    }

    @Test
    void ofList_copiesDefensively() {
      List<Block> source = new ArrayList<>();
      source.add(NotionBlocks.paragraph("a"));
      BlockListView view = BlockListView.of(source);

      source.add(NotionBlocks.paragraph("b"));
      assertEquals(1, view.size());
    }

    @Test
    void ofVarargs_wrapsBlocks() {
      BlockListView view =
          BlockListView.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));
      assertEquals(2, view.size());
    }

    @Test
    void ofNullVarargs_returnsEmptyView() {
      BlockListView view = BlockListView.of((Block[]) null);
      assertTrue(view.isEmpty());
    }

    @Test
    void ofEmptyVarargs_returnsEmptyView() {
      BlockListView view = BlockListView.of(new Block[0]);
      assertTrue(view.isEmpty());
    }
  }

  @Nested
  class TypeFiltering {

    @Test
    void ofType_filtersByExactClass() {
      BlockListView view =
          BlockListView.of(
              NotionBlocks.paragraph("p"), NotionBlocks.heading1("h1"), NotionBlocks.todo("td"));

      BlockListView paragraphs = view.ofType(ParagraphBlock.class);
      assertEquals(1, paragraphs.size());
      assertInstanceOf(ParagraphBlock.class, paragraphs.first().orElseThrow());
    }

    @Test
    void paragraphs_returnsOnlyParagraphs() {
      BlockListView view =
          BlockListView.of(
              NotionBlocks.paragraph("p1"),
              NotionBlocks.heading1("h"),
              NotionBlocks.paragraph("p2"));

      assertEquals(2, view.paragraphs().size());
    }

    @Test
    void headings_returnsAllHeadingLevels() {
      BlockListView view =
          BlockListView.of(
              NotionBlocks.heading1("h1"),
              NotionBlocks.heading2("h2"),
              NotionBlocks.heading3("h3"),
              NotionBlocks.heading4("h4"),
              NotionBlocks.paragraph("p"));

      assertEquals(4, view.headings().size());
    }

    @Test
    void todos_returnsOnlyToDoBlocks() {
      BlockListView view =
          BlockListView.of(
              NotionBlocks.todo("t1"), NotionBlocks.todo("t2"), NotionBlocks.bullet("b"));

      assertEquals(2, view.todos().size());
    }

    @Test
    void bullets_returnsOnlyBulletedListItems() {
      BlockListView view = BlockListView.of(NotionBlocks.bullet("b"), NotionBlocks.paragraph("p"));

      assertEquals(1, view.bullets().size());
      assertInstanceOf(BulletedListItemBlock.class, view.bullets().first().orElseThrow());
    }

    @Test
    void numbered_returnsOnlyNumberedListItems() {
      BlockListView view =
          BlockListView.of(NotionBlocks.numbered("n"), NotionBlocks.paragraph("p"));

      assertEquals(1, view.numbered().size());
    }

    @Test
    void toggles_returnsOnlyToggleBlocks() {
      BlockListView view = BlockListView.of(NotionBlocks.toggle("t"), NotionBlocks.paragraph("p"));

      assertEquals(1, view.toggles().size());
    }

    @Test
    void quotes_returnsOnlyQuoteBlocks() {
      BlockListView view = BlockListView.of(NotionBlocks.quote("q"), NotionBlocks.paragraph("p"));

      assertEquals(1, view.quotes().size());
    }

    @Test
    void callouts_returnsOnlyCalloutBlocks() {
      BlockListView view = BlockListView.of(NotionBlocks.callout("c"), NotionBlocks.paragraph("p"));

      assertEquals(1, view.callouts().size());
    }

    @Test
    void code_returnsOnlyCodeBlocks() {
      BlockListView view =
          BlockListView.of(NotionBlocks.code("java", "x"), NotionBlocks.paragraph("p"));

      assertEquals(1, view.code().size());
    }

    @Test
    void images_returnsOnlyImageBlocks() {
      BlockListView view =
          BlockListView.of(
              NotionBlocks.image("https://example.com/img.png"), NotionBlocks.paragraph("p"));

      assertEquals(1, view.images().size());
    }

    @Test
    void filterOnEmptyView_returnsEmptyView() {
      BlockListView view = BlockListView.of(new ArrayList<>());
      assertTrue(view.paragraphs().isEmpty());
      assertTrue(view.headings().isEmpty());
    }
  }

  @Nested
  class ToDoQueries {

    @Test
    void checked_returnsOnlyCheckedToDos() {
      ToDoBlock checkedTodo = ToDoBlock.builder().text("done").checked(true).build();
      ToDoBlock uncheckedTodo = ToDoBlock.builder().text("pending").checked(false).build();

      BlockListView view = BlockListView.of(checkedTodo, uncheckedTodo);

      assertEquals(1, view.checked().size());
      assertEquals("done", view.checked().plainText());
    }

    @Test
    void unchecked_returnsOnlyUncheckedToDos() {
      ToDoBlock checkedTodo = ToDoBlock.builder().text("done").checked(true).build();
      ToDoBlock uncheckedTodo = ToDoBlock.builder().text("pending").checked(false).build();

      BlockListView view = BlockListView.of(checkedTodo, uncheckedTodo);

      assertEquals(1, view.unchecked().size());
      assertEquals("pending", view.unchecked().plainText());
    }

    @Test
    void unchecked_treatsNullCheckedAsUnchecked() {
      ToDoBlock todo = ToDoBlock.builder().text("no state").build();

      assertEquals(1, BlockListView.of(todo).unchecked().size());
    }

    @Test
    void checked_silentlyFiltersNonToDoBlocks() {
      BlockListView view = BlockListView.of(NotionBlocks.paragraph("p"), NotionBlocks.todo("t"));

      assertEquals(0, view.checked().size());
    }

    @Test
    void unchecked_silentlyFiltersNonToDoBlocks() {
      BlockListView view = BlockListView.of(NotionBlocks.paragraph("p"));

      assertTrue(view.unchecked().isEmpty());
    }

    @Test
    void chainingTodosAndChecked() {
      ToDoBlock checked = ToDoBlock.builder().text("done").checked(true).build();
      ToDoBlock unchecked = ToDoBlock.builder().text("pending").build();

      BlockListView view = BlockListView.of(checked, unchecked, NotionBlocks.paragraph("p"));

      assertEquals(1, view.todos().checked().size());
      assertEquals(1, view.todos().unchecked().size());
    }
  }

  @Nested
  class Where {

    @Test
    void filtersWithCustomPredicate() {
      BlockListView view =
          BlockListView.of(
              NotionBlocks.paragraph("short"), NotionBlocks.paragraph("a longer paragraph text"));

      BlockListView long_ =
          view.where(
              b ->
                  b instanceof ParagraphBlock pb
                      && pb.getParagraph().getRichText().get(0).getPlainText().length() > 10);

      assertEquals(1, long_.size());
    }

    @Test
    void whereNoMatch_returnsEmptyView() {
      BlockListView view = BlockListView.of(NotionBlocks.paragraph("p"));
      assertTrue(view.where(b -> false).isEmpty());
    }
  }

  @Nested
  class PlainTextExtraction {

    @Test
    void plainText_fromParagraphs() {
      BlockListView view =
          BlockListView.of(NotionBlocks.paragraph("Hello"), NotionBlocks.paragraph("World"));

      assertEquals("Hello\nWorld", view.plainText());
    }

    @Test
    void plainText_withCustomDelimiter() {
      BlockListView view =
          BlockListView.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));

      assertEquals("a, b", view.plainText(", "));
    }

    @Test
    void plainText_fromHeadings() {
      BlockListView view =
          BlockListView.of(
              NotionBlocks.heading1("H1"),
              NotionBlocks.heading2("H2"),
              NotionBlocks.heading3("H3"),
              NotionBlocks.heading4("H4"));

      assertEquals("H1\nH2\nH3\nH4", view.plainText());
    }

    @Test
    void plainText_fromToggle() {
      assertEquals("toggle text", BlockListView.of(NotionBlocks.toggle("toggle text")).plainText());
    }

    @Test
    void plainText_fromQuote() {
      assertEquals("quoted", BlockListView.of(NotionBlocks.quote("quoted")).plainText());
    }

    @Test
    void plainText_fromCallout() {
      assertEquals("callout", BlockListView.of(NotionBlocks.callout("callout")).plainText());
    }

    @Test
    void plainText_fromBullet() {
      assertEquals("item", BlockListView.of(NotionBlocks.bullet("item")).plainText());
    }

    @Test
    void plainText_fromNumbered() {
      assertEquals("item", BlockListView.of(NotionBlocks.numbered("item")).plainText());
    }

    @Test
    void plainText_fromToDo() {
      assertEquals("task", BlockListView.of(NotionBlocks.todo("task")).plainText());
    }

    @Test
    void plainText_fromCode() {
      assertEquals(
          "int x = 5;", BlockListView.of(NotionBlocks.code("java", "int x = 5;")).plainText());
    }

    @Test
    void plainText_fromChildPage() {
      ChildPageBlock cpb = new ChildPageBlock();
      cpb.getChildPage().setTitle("My Page");
      assertEquals("My Page", BlockListView.of(cpb).plainText());
    }

    @Test
    void plainText_fromChildDatabase() {
      ChildDatabaseBlock cdb = new ChildDatabaseBlock();
      cdb.getChildDatabase().setTitle("My DB");
      assertEquals("My DB", BlockListView.of(cdb).plainText());
    }

    @Test
    void plainText_fromEquation() {
      EquationBlock eb = new EquationBlock();
      eb.getEquation().setExpression("a^2 + b^2 = c^2");
      assertEquals("a^2 + b^2 = c^2", BlockListView.of(eb).plainText());
    }

    @Test
    void plainText_skipsNonTextualBlocks() {
      BlockListView view =
          BlockListView.of(
              NotionBlocks.paragraph("text"),
              NotionBlocks.divider(),
              NotionBlocks.image("https://example.com/img.png"),
              NotionBlocks.paragraph("more text"));

      assertEquals("text\nmore text", view.plainText());
    }

    @Test
    void plainText_onEmptyView_returnsEmptyString() {
      assertEquals("", BlockListView.of(new ArrayList<>()).plainText());
    }

    @Test
    void plainTextList_returnsOneEntryPerBlock() {
      BlockListView view =
          BlockListView.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));

      List<String> texts = view.plainTextList();
      assertEquals(2, texts.size());
      assertEquals("a", texts.get(0));
      assertEquals("b", texts.get(1));
    }

    @Test
    void plainText_concatenatesMultipleRichTextSegments() {
      RichText bold = NotionText.plainText("bold ");
      RichText normal = NotionText.plainText("normal");
      ParagraphBlock p = ParagraphBlock.builder().text(bold).text(normal).build();

      assertEquals("bold normal", BlockListView.of(p).plainText());
    }
  }

  @Nested
  class Navigation {

    @Test
    void first_returnsFirstBlock() {
      ParagraphBlock p = NotionBlocks.paragraph("first");
      BlockListView view = BlockListView.of(p, NotionBlocks.paragraph("second"));

      assertSame(p, view.first().orElseThrow());
    }

    @Test
    void first_onEmptyView_returnsEmpty() {
      assertTrue(BlockListView.of(new ArrayList<>()).first().isEmpty());
    }

    @Test
    void firstWithType_returnsFirstMatchingBlock() {
      BlockListView view =
          BlockListView.of(
              NotionBlocks.paragraph("p"), NotionBlocks.heading1("h"), NotionBlocks.todo("t"));

      Optional<HeadingOneBlock> heading = view.first(HeadingOneBlock.class);
      assertTrue(heading.isPresent());
      assertEquals("h", heading.get().getHeading1().getRichText().get(0).getPlainText());
    }

    @Test
    void firstWithType_noMatch_returnsEmpty() {
      BlockListView view = BlockListView.of(NotionBlocks.paragraph("p"));

      assertTrue(view.first(ToDoBlock.class).isEmpty());
    }

    @Test
    void firstWithPredicate_returnsFirstMatch() {
      BlockListView view =
          BlockListView.of(NotionBlocks.paragraph("short"), NotionBlocks.paragraph("a long one"));

      Optional<Block> result =
          view.first(
              b ->
                  b instanceof ParagraphBlock pb
                      && pb.getParagraph().getRichText().get(0).getPlainText().length() > 5);

      assertTrue(result.isPresent());
    }

    @Test
    void firstWithPredicate_noMatch_returnsEmpty() {
      BlockListView view = BlockListView.of(NotionBlocks.paragraph("p"));
      assertTrue(view.first(b -> false).isEmpty());
    }

    @Test
    void last_returnsLastBlock() {
      ParagraphBlock last = NotionBlocks.paragraph("last");
      BlockListView view = BlockListView.of(NotionBlocks.paragraph("first"), last);

      assertSame(last, view.last().orElseThrow());
    }

    @Test
    void last_onEmptyView_returnsEmpty() {
      assertTrue(BlockListView.of(new ArrayList<>()).last().isEmpty());
    }
  }

  @Nested
  class Flatten {

    @Test
    void flatten_includesParentAndChildren() {
      ParagraphBlock parent =
          ParagraphBlock.builder()
              .text("parent")
              .children(c -> c.paragraph("child1").paragraph("child2"))
              .build();

      BlockListView flat = BlockListView.of(parent).flatten();

      assertEquals(3, flat.size());
      assertEquals("parent", flat.paragraphs().plainTextList().get(0));
      assertEquals("child1", flat.paragraphs().plainTextList().get(1));
      assertEquals("child2", flat.paragraphs().plainTextList().get(2));
    }

    @Test
    void flatten_depthFirst_parentBeforeChildren() {
      ParagraphBlock parent =
          ParagraphBlock.builder().text("parent").children(c -> c.paragraph("child")).build();

      BlockListView flat = BlockListView.of(parent).flatten();
      List<String> texts = flat.plainTextList();

      assertEquals("parent", texts.get(0));
      assertEquals("child", texts.get(1));
    }

    @Test
    void flatten_toggle_includesNestedContent() {
      ToggleBlock toggle =
          ToggleBlock.builder().text("toggle").children(c -> c.paragraph("inside")).build();

      BlockListView flat = BlockListView.of(toggle).flatten();
      assertEquals(2, flat.size());
      assertEquals("inside", flat.paragraphs().plainText());
    }

    @Test
    void flatten_multiLevel() {
      ParagraphBlock deep =
          ParagraphBlock.builder()
              .text("level1")
              .children(
                  c ->
                      c.block(
                          ParagraphBlock.builder()
                              .text("level2")
                              .children(ch -> ch.paragraph("level3"))
                              .build()))
              .build();

      BlockListView flat = BlockListView.of(deep).flatten();
      List<String> texts = flat.plainTextList();
      assertEquals(3, texts.size());
      assertEquals("level1", texts.get(0));
      assertEquals("level2", texts.get(1));
      assertEquals("level3", texts.get(2));
    }

    @Test
    void flatten_columnList_traversesColumns() {
      ColumnBlock col1 = new ColumnBlock();
      col1.getColumn().getChildren().add(NotionBlocks.paragraph("col1-text"));

      ColumnBlock col2 = new ColumnBlock();
      col2.getColumn().getChildren().add(NotionBlocks.paragraph("col2-text"));

      ColumnListBlock colList = new ColumnListBlock();
      colList.getColumnList().setChildren(List.of(col1, col2));

      BlockListView flat = BlockListView.of(colList).flatten();
      assertEquals(5, flat.size());
      assertEquals(2, flat.paragraphs().size());
      assertEquals("col1-text\ncol2-text", flat.paragraphs().plainText());
    }

    @Test
    void flatten_synced_traversesChildren() {
      SyncedBlock synced = new SyncedBlock();
      synced.getSyncedBlock().setChildren(List.of(NotionBlocks.paragraph("synced-child")));

      BlockListView flat = BlockListView.of(synced).flatten();
      assertEquals(2, flat.size());
      assertEquals("synced-child", flat.paragraphs().plainText());
    }

    @Test
    void flatten_blockWithNoChildren_returnsSingleBlock() {
      BlockListView flat = BlockListView.of(NotionBlocks.paragraph("single")).flatten();
      assertEquals(1, flat.size());
    }

    @Test
    void flatten_emptyView_returnsEmptyView() {
      assertTrue(BlockListView.of(new ArrayList<>()).flatten().isEmpty());
    }
  }

  @Nested
  class TypedAccess {

    @Test
    void as_castsAllBlocksToType() {
      BlockListView view =
          BlockListView.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));

      List<ParagraphBlock> paragraphs = view.paragraphs().as(ParagraphBlock.class);
      assertEquals(2, paragraphs.size());
      assertInstanceOf(ParagraphBlock.class, paragraphs.get(0));
    }

    @Test
    void as_throwsOnTypeMismatch() {
      BlockListView view = BlockListView.of(NotionBlocks.paragraph("p"));

      assertThrows(ClassCastException.class, () -> view.as(ToDoBlock.class));
    }

    @Test
    void blocks_returnsUnmodifiableList() {
      BlockListView view = BlockListView.of(NotionBlocks.paragraph("p"));
      assertThrows(
          UnsupportedOperationException.class, () -> view.blocks().add(new ParagraphBlock()));
    }

    @Test
    void stream_enablesAdvancedOperations() {
      BlockListView view =
          BlockListView.of(
              NotionBlocks.paragraph("p"), NotionBlocks.heading1("h"), NotionBlocks.todo("t"));

      long count = view.stream().filter(b -> b instanceof ParagraphBlock).count();
      assertEquals(1, count);
    }
  }

  @Nested
  class CollectionOperations {

    @Test
    void size_returnsBlockCount() {
      assertEquals(
          3,
          BlockListView.of(
                  NotionBlocks.paragraph("a"),
                  NotionBlocks.paragraph("b"),
                  NotionBlocks.paragraph("c"))
              .size());
    }

    @Test
    void isEmpty_trueForEmptyView() {
      assertTrue(BlockListView.of(new ArrayList<>()).isEmpty());
    }

    @Test
    void isEmpty_falseForNonEmptyView() {
      assertFalse(BlockListView.of(NotionBlocks.paragraph("p")).isEmpty());
    }

    @Test
    void forEach_iteratesAllBlocks() {
      BlockListView view =
          BlockListView.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));

      List<Block> visited = new ArrayList<>();
      view.forEach(visited::add);
      assertEquals(2, visited.size());
    }

    @Test
    void forEachTyped_iteratesOnlyMatchingBlocks() {
      BlockListView view =
          BlockListView.of(
              NotionBlocks.paragraph("p"), NotionBlocks.heading1("h"), NotionBlocks.todo("t"));

      List<ParagraphBlock> paragraphs = new ArrayList<>();
      view.forEach(ParagraphBlock.class, paragraphs::add);

      assertEquals(1, paragraphs.size());
    }

    @Test
    void iterator_worksWithEnhancedForLoop() {
      BlockListView view =
          BlockListView.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));

      int count = 0;
      for (Block b : view) {
        assertInstanceOf(ParagraphBlock.class, b);
        count++;
      }
      assertEquals(2, count);
    }

    @Test
    void iterator_isUnmodifiable() {
      BlockListView view = BlockListView.of(NotionBlocks.paragraph("a"));
      var it = view.iterator();
      it.next();
      assertThrows(UnsupportedOperationException.class, it::remove);
    }
  }

  @Nested
  class ChainingWorkflows {

    @Test
    void filterThenExtractText() {
      BlockListView view =
          BlockListView.of(
              NotionBlocks.heading1("Title"),
              NotionBlocks.paragraph("Body"),
              NotionBlocks.todo("Task"));

      assertEquals("Title", view.headings().plainText());
      assertEquals("Body", view.paragraphs().plainText());
      assertEquals("Task", view.todos().plainText());
    }

    @Test
    void flattenThenFilterThenExtract() {
      ToggleBlock toggle =
          ToggleBlock.builder()
              .text("toggle")
              .children(c -> c.paragraph("nested paragraph"))
              .build();

      BlockListView view = BlockListView.of(toggle, NotionBlocks.paragraph("top-level"));

      String allParaText = view.flatten().paragraphs().plainText();
      assertEquals("nested paragraph\ntop-level", allParaText);
    }

    @Test
    void todosCheckedUncheckedPlainText() {
      ToDoBlock done = ToDoBlock.builder().text("completed").checked(true).build();
      ToDoBlock pending = ToDoBlock.builder().text("pending").build();

      BlockListView view = BlockListView.of(done, pending);

      assertEquals("completed", view.todos().checked().plainText());
      assertEquals("pending", view.todos().unchecked().plainText());
    }
  }
}
