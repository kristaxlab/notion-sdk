package io.kristaxlab.notion.fluent;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.model.block.*;
import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NotionBlocksViewerTest {

  @Nested
  class Factories {

    @Test
    @DisplayName("of null list returns empty view")
    void ofNullList_returnsEmptyView() {
      NotionBlocksViewer view = NotionBlocksViewer.of((List<Block>) null);
      assertTrue(view.isEmpty());
      assertEquals(0, view.size());
    }

    @Test
    @DisplayName("of empty list returns empty view")
    void ofEmptyList_returnsEmptyView() {
      NotionBlocksViewer view = NotionBlocksViewer.of(new ArrayList<>());
      assertTrue(view.isEmpty());
    }

    @Test
    @DisplayName("of list copies defensively")
    void ofList_copiesDefensively() {
      List<Block> source = new ArrayList<>();
      source.add(NotionBlocks.paragraph("a"));
      NotionBlocksViewer view = NotionBlocksViewer.of(source);

      source.add(NotionBlocks.paragraph("b"));
      assertEquals(1, view.size());
    }

    @Test
    @DisplayName("of varargs wraps blocks")
    void ofVarargs_wrapsBlocks() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));
      assertEquals(2, view.size());
    }

    @Test
    @DisplayName("of null varargs returns empty view")
    void ofNullVarargs_returnsEmptyView() {
      NotionBlocksViewer view = NotionBlocksViewer.of((Block[]) null);
      assertTrue(view.isEmpty());
    }

    @Test
    @DisplayName("of empty varargs returns empty view")
    void ofEmptyVarargs_returnsEmptyView() {
      NotionBlocksViewer view = NotionBlocksViewer.of(new Block[0]);
      assertTrue(view.isEmpty());
    }

    @Test
    @DisplayName("of block list wraps results")
    void ofBlockList_wrapsResults() {
      BlockList blockList = new BlockList();
      blockList.setResults(List.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b")));
      NotionBlocksViewer view = NotionBlocksViewer.of(blockList);
      assertEquals(2, view.size());
    }

    @Test
    @DisplayName("of null block list returns empty view")
    void ofNullBlockList_returnsEmptyView() {
      NotionBlocksViewer view = NotionBlocksViewer.of((BlockList) null);
      assertTrue(view.isEmpty());
    }

    @Test
    @DisplayName("of block list with null results returns empty view")
    void ofBlockListWithNullResults_returnsEmptyView() {
      BlockList blockList = new BlockList();
      blockList.setResults(null);
      NotionBlocksViewer view = NotionBlocksViewer.of(blockList);
      assertTrue(view.isEmpty());
    }

    @Test
    @DisplayName("of block list with empty results returns empty view")
    void ofBlockListWithEmptyResults_returnsEmptyView() {
      BlockList blockList = new BlockList();
      blockList.setResults(new ArrayList<>());
      NotionBlocksViewer view = NotionBlocksViewer.of(blockList);
      assertTrue(view.isEmpty());
    }
  }

  @Nested
  class TypeFiltering {

    @Test
    @DisplayName("of type filters by exact class")
    void ofType_filtersByExactClass() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.paragraph("p"), NotionBlocks.heading1("h1"), NotionBlocks.todo("td"));

      NotionBlocksViewer paragraphs = view.ofType(ParagraphBlock.class);
      assertEquals(1, paragraphs.size());
      assertInstanceOf(ParagraphBlock.class, paragraphs.first().orElseThrow());
    }

    @Test
    @DisplayName("paragraphs returns only paragraphs")
    void paragraphs_returnsOnlyParagraphs() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.paragraph("p1"),
              NotionBlocks.heading1("h"),
              NotionBlocks.paragraph("p2"));

      assertEquals(2, view.paragraphs().size());
    }

    @Test
    @DisplayName("headings returns all heading levels")
    void headings_returnsAllHeadingLevels() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.heading1("h1"),
              NotionBlocks.heading2("h2"),
              NotionBlocks.heading3("h3"),
              NotionBlocks.heading4("h4"),
              NotionBlocks.paragraph("p"));

      assertEquals(4, view.headings().size());
    }

    @Test
    @DisplayName("todos returns only to do blocks")
    void todos_returnsOnlyToDoBlocks() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.todo("t1"), NotionBlocks.todo("t2"), NotionBlocks.bullet("b"));

      assertEquals(2, view.todos().size());
    }

    @Test
    @DisplayName("bullets returns only bulleted list items")
    void bullets_returnsOnlyBulletedListItems() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(NotionBlocks.bullet("b"), NotionBlocks.paragraph("p"));

      assertEquals(1, view.bullets().size());
      assertInstanceOf(BulletedListItemBlock.class, view.bullets().first().orElseThrow());
    }

    @Test
    @DisplayName("numbered returns only numbered list items")
    void numbered_returnsOnlyNumberedListItems() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(NotionBlocks.numbered("n"), NotionBlocks.paragraph("p"));

      assertEquals(1, view.numbered().size());
    }

    @Test
    @DisplayName("toggles returns only toggle blocks")
    void toggles_returnsOnlyToggleBlocks() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(NotionBlocks.toggle("t"), NotionBlocks.paragraph("p"));

      assertEquals(1, view.toggles().size());
    }

    @Test
    @DisplayName("quotes returns only quote blocks")
    void quotes_returnsOnlyQuoteBlocks() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(NotionBlocks.quote("q"), NotionBlocks.paragraph("p"));

      assertEquals(1, view.quotes().size());
    }

    @Test
    @DisplayName("callouts returns only callout blocks")
    void callouts_returnsOnlyCalloutBlocks() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(NotionBlocks.callout("c"), NotionBlocks.paragraph("p"));

      assertEquals(1, view.callouts().size());
    }

    @Test
    @DisplayName("code returns only code blocks")
    void code_returnsOnlyCodeBlocks() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(NotionBlocks.code("java", "x"), NotionBlocks.paragraph("p"));

      assertEquals(1, view.code().size());
    }

    @Test
    @DisplayName("images returns only image blocks")
    void images_returnsOnlyImageBlocks() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.image("https://example.com/img.png"), NotionBlocks.paragraph("p"));

      assertEquals(1, view.images().size());
    }

    @Test
    @DisplayName("filter on empty view returns empty view")
    void filterOnEmptyView_returnsEmptyView() {
      NotionBlocksViewer view = NotionBlocksViewer.of(new ArrayList<>());
      assertTrue(view.paragraphs().isEmpty());
      assertTrue(view.headings().isEmpty());
    }
  }

  @Nested
  class ToDoQueries {

    @Test
    @DisplayName("checked returns only checked to dos")
    void checked_returnsOnlyCheckedToDos() {
      ToDoBlock checkedTodo = ToDoBlock.builder().text("done").checked(true).build();
      ToDoBlock uncheckedTodo = ToDoBlock.builder().text("pending").checked(false).build();

      NotionBlocksViewer view = NotionBlocksViewer.of(checkedTodo, uncheckedTodo);

      assertEquals(1, view.checked().size());
      assertEquals("done", view.checked().plainText());
    }

    @Test
    @DisplayName("unchecked returns only unchecked to dos")
    void unchecked_returnsOnlyUncheckedToDos() {
      ToDoBlock checkedTodo = ToDoBlock.builder().text("done").checked(true).build();
      ToDoBlock uncheckedTodo = ToDoBlock.builder().text("pending").checked(false).build();

      NotionBlocksViewer view = NotionBlocksViewer.of(checkedTodo, uncheckedTodo);

      assertEquals(1, view.unchecked().size());
      assertEquals("pending", view.unchecked().plainText());
    }

    @Test
    @DisplayName("unchecked treats null checked as unchecked")
    void unchecked_treatsNullCheckedAsUnchecked() {
      ToDoBlock todo = ToDoBlock.builder().text("no state").build();

      assertEquals(1, NotionBlocksViewer.of(todo).unchecked().size());
    }

    @Test
    @DisplayName("checked silently filters non to do blocks")
    void checked_silentlyFiltersNonToDoBlocks() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(NotionBlocks.paragraph("p"), NotionBlocks.todo("t"));

      assertEquals(0, view.checked().size());
    }

    @Test
    @DisplayName("unchecked silently filters non to do blocks")
    void unchecked_silentlyFiltersNonToDoBlocks() {
      NotionBlocksViewer view = NotionBlocksViewer.of(NotionBlocks.paragraph("p"));

      assertTrue(view.unchecked().isEmpty());
    }

    @Test
    @DisplayName("chaining todos and checked")
    void chainingTodosAndChecked() {
      ToDoBlock checked = ToDoBlock.builder().text("done").checked(true).build();
      ToDoBlock unchecked = ToDoBlock.builder().text("pending").build();

      NotionBlocksViewer view =
          NotionBlocksViewer.of(checked, unchecked, NotionBlocks.paragraph("p"));

      assertEquals(1, view.todos().checked().size());
      assertEquals(1, view.todos().unchecked().size());
    }
  }

  @Nested
  class Where {

    @Test
    @DisplayName("filters with custom predicate")
    void filtersWithCustomPredicate() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.paragraph("short"), NotionBlocks.paragraph("a longer paragraph text"));

      NotionBlocksViewer long_ =
          view.where(
              b ->
                  b instanceof ParagraphBlock pb
                      && pb.getParagraph().getRichText().get(0).getPlainText().length() > 10);

      assertEquals(1, long_.size());
    }

    @Test
    @DisplayName("where no match returns empty view")
    void whereNoMatch_returnsEmptyView() {
      NotionBlocksViewer view = NotionBlocksViewer.of(NotionBlocks.paragraph("p"));
      assertTrue(view.where(b -> false).isEmpty());
    }
  }

  @Nested
  class PlainTextExtraction {

    @Test
    @DisplayName("plain text from paragraphs")
    void plainText_fromParagraphs() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(NotionBlocks.paragraph("Hello"), NotionBlocks.paragraph("World"));

      assertEquals("Hello\nWorld", view.plainText());
    }

    @Test
    @DisplayName("plain text with custom delimiter")
    void plainText_withCustomDelimiter() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));

      assertEquals("a, b", view.plainText(", "));
    }

    @Test
    @DisplayName("plain text from headings")
    void plainText_fromHeadings() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.heading1("H1"),
              NotionBlocks.heading2("H2"),
              NotionBlocks.heading3("H3"),
              NotionBlocks.heading4("H4"));

      assertEquals("H1\nH2\nH3\nH4", view.plainText());
    }

    @Test
    @DisplayName("plain text from toggle")
    void plainText_fromToggle() {
      assertEquals(
          "toggle text", NotionBlocksViewer.of(NotionBlocks.toggle("toggle text")).plainText());
    }

    @Test
    @DisplayName("plain text from quote")
    void plainText_fromQuote() {
      assertEquals("quoted", NotionBlocksViewer.of(NotionBlocks.quote("quoted")).plainText());
    }

    @Test
    @DisplayName("plain text from callout")
    void plainText_fromCallout() {
      assertEquals("callout", NotionBlocksViewer.of(NotionBlocks.callout("callout")).plainText());
    }

    @Test
    @DisplayName("plain text from bullet")
    void plainText_fromBullet() {
      assertEquals("item", NotionBlocksViewer.of(NotionBlocks.bullet("item")).plainText());
    }

    @Test
    @DisplayName("plain text from numbered")
    void plainText_fromNumbered() {
      assertEquals("item", NotionBlocksViewer.of(NotionBlocks.numbered("item")).plainText());
    }

    @Test
    @DisplayName("plain text from to do")
    void plainText_fromToDo() {
      assertEquals("task", NotionBlocksViewer.of(NotionBlocks.todo("task")).plainText());
    }

    @Test
    @DisplayName("plain text from code")
    void plainText_fromCode() {
      assertEquals(
          "int x = 5;", NotionBlocksViewer.of(NotionBlocks.code("java", "int x = 5;")).plainText());
    }

    @Test
    @DisplayName("plain text from child page")
    void plainText_fromChildPage() {
      ChildPageBlock cpb = new ChildPageBlock();
      cpb.getChildPage().setTitle("My Page");
      assertEquals("My Page", NotionBlocksViewer.of(cpb).plainText());
    }

    @Test
    @DisplayName("plain text from child database")
    void plainText_fromChildDatabase() {
      ChildDatabaseBlock cdb = new ChildDatabaseBlock();
      cdb.getChildDatabase().setTitle("My DB");
      assertEquals("My DB", NotionBlocksViewer.of(cdb).plainText());
    }

    @Test
    @DisplayName("plain text from equation")
    void plainText_fromEquation() {
      EquationBlock eb = new EquationBlock();
      eb.getEquation().setExpression("a^2 + b^2 = c^2");
      assertEquals("a^2 + b^2 = c^2", NotionBlocksViewer.of(eb).plainText());
    }

    @Test
    @DisplayName("plain text skips non textual blocks")
    void plainText_skipsNonTextualBlocks() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.paragraph("text"),
              NotionBlocks.divider(),
              NotionBlocks.image("https://example.com/img.png"),
              NotionBlocks.paragraph("more text"));

      assertEquals("text\nmore text", view.plainText());
    }

    @Test
    @DisplayName("plain text on empty view returns empty string")
    void plainText_onEmptyView_returnsEmptyString() {
      assertEquals("", NotionBlocksViewer.of(new ArrayList<>()).plainText());
    }

    @Test
    @DisplayName("plain text list returns one entry per block")
    void plainTextList_returnsOneEntryPerBlock() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));

      List<String> texts = view.plainTextList();
      assertEquals(2, texts.size());
      assertEquals("a", texts.get(0));
      assertEquals("b", texts.get(1));
    }

    @Test
    @DisplayName("plain text concatenates multiple rich text segments")
    void plainText_concatenatesMultipleRichTextSegments() {
      RichText bold = NotionText.plainText("bold ");
      RichText normal = NotionText.plainText("normal");
      ParagraphBlock p = ParagraphBlock.builder().text(bold).text(normal).build();

      assertEquals("bold normal", NotionBlocksViewer.of(p).plainText());
    }
  }

  @Nested
  class Navigation {

    @Test
    @DisplayName("first returns first block")
    void first_returnsFirstBlock() {
      ParagraphBlock p = NotionBlocks.paragraph("first");
      NotionBlocksViewer view = NotionBlocksViewer.of(p, NotionBlocks.paragraph("second"));

      assertSame(p, view.first().orElseThrow());
    }

    @Test
    @DisplayName("first on empty view returns empty")
    void first_onEmptyView_returnsEmpty() {
      assertTrue(NotionBlocksViewer.of(new ArrayList<>()).first().isEmpty());
    }

    @Test
    @DisplayName("first with type returns first matching block")
    void firstWithType_returnsFirstMatchingBlock() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.paragraph("p"), NotionBlocks.heading1("h"), NotionBlocks.todo("t"));

      Optional<HeadingOneBlock> heading = view.first(HeadingOneBlock.class);
      assertTrue(heading.isPresent());
      assertEquals("h", heading.get().getHeading1().getRichText().get(0).getPlainText());
    }

    @Test
    @DisplayName("first with type no match returns empty")
    void firstWithType_noMatch_returnsEmpty() {
      NotionBlocksViewer view = NotionBlocksViewer.of(NotionBlocks.paragraph("p"));

      assertTrue(view.first(ToDoBlock.class).isEmpty());
    }

    @Test
    @DisplayName("first with predicate returns first match")
    void firstWithPredicate_returnsFirstMatch() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.paragraph("short"), NotionBlocks.paragraph("a long one"));

      Optional<Block> result =
          view.first(
              b ->
                  b instanceof ParagraphBlock pb
                      && pb.getParagraph().getRichText().get(0).getPlainText().length() > 5);

      assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("first with predicate no match returns empty")
    void firstWithPredicate_noMatch_returnsEmpty() {
      NotionBlocksViewer view = NotionBlocksViewer.of(NotionBlocks.paragraph("p"));
      assertTrue(view.first(b -> false).isEmpty());
    }

    @Test
    @DisplayName("last returns last block")
    void last_returnsLastBlock() {
      ParagraphBlock last = NotionBlocks.paragraph("last");
      NotionBlocksViewer view = NotionBlocksViewer.of(NotionBlocks.paragraph("first"), last);

      assertSame(last, view.last().orElseThrow());
    }

    @Test
    @DisplayName("last on empty view returns empty")
    void last_onEmptyView_returnsEmpty() {
      assertTrue(NotionBlocksViewer.of(new ArrayList<>()).last().isEmpty());
    }
  }

  @Nested
  class Flatten {

    @Test
    @DisplayName("flatten includes parent and children")
    void flatten_includesParentAndChildren() {
      ParagraphBlock parent =
          ParagraphBlock.builder()
              .text("parent")
              .children(c -> c.paragraph("child1").paragraph("child2"))
              .build();

      NotionBlocksViewer flat = NotionBlocksViewer.of(parent).flatten();

      assertEquals(3, flat.size());
      assertEquals("parent", flat.paragraphs().plainTextList().get(0));
      assertEquals("child1", flat.paragraphs().plainTextList().get(1));
      assertEquals("child2", flat.paragraphs().plainTextList().get(2));
    }

    @Test
    @DisplayName("flatten depth first parent before children")
    void flatten_depthFirst_parentBeforeChildren() {
      ParagraphBlock parent =
          ParagraphBlock.builder().text("parent").children(c -> c.paragraph("child")).build();

      NotionBlocksViewer flat = NotionBlocksViewer.of(parent).flatten();
      List<String> texts = flat.plainTextList();

      assertEquals("parent", texts.get(0));
      assertEquals("child", texts.get(1));
    }

    @Test
    @DisplayName("flatten toggle includes nested content")
    void flatten_toggle_includesNestedContent() {
      ToggleBlock toggle =
          ToggleBlock.builder().text("toggle").children(c -> c.paragraph("inside")).build();

      NotionBlocksViewer flat = NotionBlocksViewer.of(toggle).flatten();
      assertEquals(2, flat.size());
      assertEquals("inside", flat.paragraphs().plainText());
    }

    @Test
    @DisplayName("flatten multi level")
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

      NotionBlocksViewer flat = NotionBlocksViewer.of(deep).flatten();
      List<String> texts = flat.plainTextList();
      assertEquals(3, texts.size());
      assertEquals("level1", texts.get(0));
      assertEquals("level2", texts.get(1));
      assertEquals("level3", texts.get(2));
    }

    @Test
    @DisplayName("flatten column list traverses columns")
    void flatten_columnList_traversesColumns() {
      ColumnBlock col1 = new ColumnBlock();
      col1.getColumn().getChildren().add(NotionBlocks.paragraph("col1-text"));

      ColumnBlock col2 = new ColumnBlock();
      col2.getColumn().getChildren().add(NotionBlocks.paragraph("col2-text"));

      ColumnListBlock colList = new ColumnListBlock();
      colList.getColumnList().setChildren(List.of(col1, col2));

      NotionBlocksViewer flat = NotionBlocksViewer.of(colList).flatten();
      assertEquals(5, flat.size());
      assertEquals(2, flat.paragraphs().size());
      assertEquals("col1-text\ncol2-text", flat.paragraphs().plainText());
    }

    @Test
    @DisplayName("flatten synced traverses children")
    void flatten_synced_traversesChildren() {
      SyncedBlock synced = new SyncedBlock();
      synced.getSyncedBlock().setChildren(List.of(NotionBlocks.paragraph("synced-child")));

      NotionBlocksViewer flat = NotionBlocksViewer.of(synced).flatten();
      assertEquals(2, flat.size());
      assertEquals("synced-child", flat.paragraphs().plainText());
    }

    @Test
    @DisplayName("flatten block with no children returns single block")
    void flatten_blockWithNoChildren_returnsSingleBlock() {
      NotionBlocksViewer flat = NotionBlocksViewer.of(NotionBlocks.paragraph("single")).flatten();
      assertEquals(1, flat.size());
    }

    @Test
    @DisplayName("flatten empty view returns empty view")
    void flatten_emptyView_returnsEmptyView() {
      assertTrue(NotionBlocksViewer.of(new ArrayList<>()).flatten().isEmpty());
    }
  }

  @Nested
  class TypedAccess {

    @Test
    @DisplayName("as casts all blocks to type")
    void as_castsAllBlocksToType() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));

      List<ParagraphBlock> paragraphs = view.paragraphs().as(ParagraphBlock.class);
      assertEquals(2, paragraphs.size());
      assertInstanceOf(ParagraphBlock.class, paragraphs.get(0));
    }

    @Test
    @DisplayName("as throws on type mismatch")
    void as_throwsOnTypeMismatch() {
      NotionBlocksViewer view = NotionBlocksViewer.of(NotionBlocks.paragraph("p"));

      assertThrows(ClassCastException.class, () -> view.as(ToDoBlock.class));
    }

    @Test
    @DisplayName("blocks returns unmodifiable list")
    void blocks_returnsUnmodifiableList() {
      NotionBlocksViewer view = NotionBlocksViewer.of(NotionBlocks.paragraph("p"));
      assertThrows(
          UnsupportedOperationException.class, () -> view.blocks().add(new ParagraphBlock()));
    }

    @Test
    @DisplayName("stream enables advanced operations")
    void stream_enablesAdvancedOperations() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.paragraph("p"), NotionBlocks.heading1("h"), NotionBlocks.todo("t"));

      long count = view.stream().filter(b -> b instanceof ParagraphBlock).count();
      assertEquals(1, count);
    }
  }

  @Nested
  class CollectionOperations {

    @Test
    @DisplayName("size returns block count")
    void size_returnsBlockCount() {
      assertEquals(
          3,
          NotionBlocksViewer.of(
                  NotionBlocks.paragraph("a"),
                  NotionBlocks.paragraph("b"),
                  NotionBlocks.paragraph("c"))
              .size());
    }

    @Test
    @DisplayName("is empty true for empty view")
    void isEmpty_trueForEmptyView() {
      assertTrue(NotionBlocksViewer.of(new ArrayList<>()).isEmpty());
    }

    @Test
    @DisplayName("is empty false for non empty view")
    void isEmpty_falseForNonEmptyView() {
      assertFalse(NotionBlocksViewer.of(NotionBlocks.paragraph("p")).isEmpty());
    }

    @Test
    @DisplayName("for each iterates all blocks")
    void forEach_iteratesAllBlocks() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));

      List<Block> visited = new ArrayList<>();
      view.forEach(visited::add);
      assertEquals(2, visited.size());
    }

    @Test
    @DisplayName("for each typed iterates only matching blocks")
    void forEachTyped_iteratesOnlyMatchingBlocks() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.paragraph("p"), NotionBlocks.heading1("h"), NotionBlocks.todo("t"));

      List<ParagraphBlock> paragraphs = new ArrayList<>();
      view.forEach(ParagraphBlock.class, paragraphs::add);

      assertEquals(1, paragraphs.size());
    }

    @Test
    @DisplayName("iterator works with enhanced for loop")
    void iterator_worksWithEnhancedForLoop() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));

      int count = 0;
      for (Block b : view) {
        assertInstanceOf(ParagraphBlock.class, b);
        count++;
      }
      assertEquals(2, count);
    }

    @Test
    @DisplayName("iterator is unmodifiable")
    void iterator_isUnmodifiable() {
      NotionBlocksViewer view = NotionBlocksViewer.of(NotionBlocks.paragraph("a"));
      var it = view.iterator();
      it.next();
      assertThrows(UnsupportedOperationException.class, it::remove);
    }
  }

  @Nested
  class ChainingWorkflows {

    @Test
    @DisplayName("filter then extract text")
    void filterThenExtractText() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.heading1("Title"),
              NotionBlocks.paragraph("Body"),
              NotionBlocks.todo("Task"));

      assertEquals("Title", view.headings().plainText());
      assertEquals("Body", view.paragraphs().plainText());
      assertEquals("Task", view.todos().plainText());
    }

    @Test
    @DisplayName("flatten then filter then extract")
    void flattenThenFilterThenExtract() {
      ToggleBlock toggle =
          ToggleBlock.builder()
              .text("toggle")
              .children(c -> c.paragraph("nested paragraph"))
              .build();

      NotionBlocksViewer view = NotionBlocksViewer.of(toggle, NotionBlocks.paragraph("top-level"));

      String allParaText = view.flatten().paragraphs().plainText();
      assertEquals("nested paragraph\ntop-level", allParaText);
    }

    @Test
    @DisplayName("todos checked unchecked plain text")
    void todosCheckedUncheckedPlainText() {
      ToDoBlock done = ToDoBlock.builder().text("completed").checked(true).build();
      ToDoBlock pending = ToDoBlock.builder().text("pending").build();

      NotionBlocksViewer view = NotionBlocksViewer.of(done, pending);

      assertEquals("completed", view.todos().checked().plainText());
      assertEquals("pending", view.todos().unchecked().plainText());
    }
  }

  @Nested
  class Containing {

    @Test
    @DisplayName("matches by plain text content")
    void matchesByPlainTextContent() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.paragraph("Hello world"),
              NotionBlocks.paragraph("Goodbye"),
              NotionBlocks.heading1("Hello heading"));

      NotionBlocksViewer result = view.containing("hello");
      assertEquals(2, result.size());
    }

    @Test
    @DisplayName("matches across multiple rich text segments")
    void matchesAcrossMultipleRichTextSegments() {
      RichText part1 = NotionText.plainText("Hel");
      RichText part2 = NotionText.plainText("lo world");
      ParagraphBlock p = ParagraphBlock.builder().text(part1).text(part2).build();

      NotionBlocksViewer view = NotionBlocksViewer.of(p, NotionBlocks.paragraph("other"));
      assertEquals(1, view.containing("hello").size());
    }

    @Test
    @DisplayName("matches by rich text href")
    void matchesByRichTextHref() {
      RichText link = NotionText.url("https://example.com/page");
      ParagraphBlock p = ParagraphBlock.builder().text(link).build();

      NotionBlocksViewer view = NotionBlocksViewer.of(p);
      assertEquals(1, view.containing("example.com").size());
    }

    @Test
    @DisplayName("matches by bookmark url")
    void matchesByBookmarkUrl() {
      BookmarkBlock bookmark = NotionBlocks.bookmark("https://notion.so/docs");

      assertEquals(1, NotionBlocksViewer.of(bookmark).containing("notion.so").size());
    }

    @Test
    @DisplayName("matches by embed url")
    void matchesByEmbedUrl() {
      EmbedBlock embed = NotionBlocks.embed("https://youtube.com/watch?v=123");

      assertEquals(1, NotionBlocksViewer.of(embed).containing("youtube").size());
    }

    @Test
    @DisplayName("matches by link preview url")
    void matchesByLinkPreviewUrl() {
      LinkPreviewBlock lp = new LinkPreviewBlock();
      lp.getLinkPreview().setUrl("https://github.com/repo");

      assertEquals(1, NotionBlocksViewer.of(lp).containing("github").size());
    }

    @Test
    @DisplayName("matches by child page title")
    void matchesByChildPageTitle() {
      ChildPageBlock cpb = new ChildPageBlock();
      cpb.getChildPage().setTitle("Project Roadmap");

      assertEquals(1, NotionBlocksViewer.of(cpb).containing("roadmap").size());
    }

    @Test
    @DisplayName("matches by equation expression")
    void matchesByEquationExpression() {
      EquationBlock eb = new EquationBlock();
      eb.getEquation().setExpression("E = mc^2");

      assertEquals(1, NotionBlocksViewer.of(eb).containing("mc^2").size());
    }

    @Test
    @DisplayName("no match returns empty")
    void noMatch_returnsEmpty() {
      NotionBlocksViewer view = NotionBlocksViewer.of(NotionBlocks.paragraph("Hello"));
      assertTrue(view.containing("xyz").isEmpty());
    }

    @Test
    @DisplayName("case insensitive")
    void caseInsensitive() {
      NotionBlocksViewer view = NotionBlocksViewer.of(NotionBlocks.paragraph("Hello WORLD"));
      assertEquals(1, view.containing("hello world").size());
    }

    @Test
    @DisplayName("matches by image url")
    void matchesByImageUrl() {
      ImageBlock img = NotionBlocks.image("https://cdn.example.com/photo.jpg");
      assertEquals(1, NotionBlocksViewer.of(img).containing("cdn.example.com").size());
    }

    @Test
    @DisplayName("null keyword throws npe")
    void nullKeyword_throwsNpe() {
      NotionBlocksViewer view = NotionBlocksViewer.of(NotionBlocks.paragraph("text"));
      assertThrows(NullPointerException.class, () -> view.containing(null));
    }

    @Test
    @DisplayName("matches in direct child")
    void matchesInDirectChild() {
      ParagraphBlock parent =
          ParagraphBlock.builder()
              .text("parent text")
              .children(c -> c.paragraph("child keyword here"))
              .build();

      assertEquals(1, NotionBlocksViewer.of(parent).containing("keyword").size());
    }

    @Test
    @DisplayName("matches in deeply nested child")
    void matchesInDeeplyNestedChild() {
      ParagraphBlock root =
          ParagraphBlock.builder()
              .text("level1")
              .children(
                  c ->
                      c.block(
                          ParagraphBlock.builder()
                              .text("level2")
                              .children(ch -> ch.paragraph("deep keyword"))
                              .build()))
              .build();

      assertEquals(1, NotionBlocksViewer.of(root).containing("deep keyword").size());
    }

    @Test
    @DisplayName("parent match short circuits before checking children")
    void parentMatchShortCircuitsBeforeCheckingChildren() {
      ParagraphBlock parent =
          ParagraphBlock.builder()
              .text("keyword in parent")
              .children(c -> c.paragraph("child text"))
              .build();

      assertEquals(1, NotionBlocksViewer.of(parent).containing("keyword").size());
    }

    @Test
    @DisplayName("no match in parent or children returns empty")
    void noMatchInParentOrChildren_returnsEmpty() {
      ParagraphBlock parent =
          ParagraphBlock.builder().text("hello").children(c -> c.paragraph("world")).build();

      assertTrue(NotionBlocksViewer.of(parent).containing("xyz").isEmpty());
    }

    @Test
    @DisplayName("matches in toggle child block")
    void matchesInToggleChildBlock() {
      ToggleBlock toggle =
          ToggleBlock.builder()
              .text("toggle label")
              .children(c -> c.paragraph("hidden keyword"))
              .build();

      assertEquals(1, NotionBlocksViewer.of(toggle).containing("hidden keyword").size());
    }
  }

  @Nested
  class Links {

    @Test
    @DisplayName("extracts hrefs from rich text")
    void extractsHrefsFromRichText() {
      RichText link = NotionText.url("https://example.com");
      ParagraphBlock p = ParagraphBlock.builder().text(link).build();

      List<String> urls = NotionBlocksViewer.of(p).links();
      assertEquals(1, urls.size());
      assertEquals("https://example.com", urls.get(0));
    }

    @Test
    @DisplayName("extracts bookmark url")
    void extractsBookmarkUrl() {
      BookmarkBlock bookmark = NotionBlocks.bookmark("https://notion.so");

      List<String> urls = NotionBlocksViewer.of(bookmark).links();
      assertTrue(urls.contains("https://notion.so"));
    }

    @Test
    @DisplayName("extracts embed url")
    void extractsEmbedUrl() {
      EmbedBlock embed = NotionBlocks.embed("https://youtube.com/watch");

      List<String> urls = NotionBlocksViewer.of(embed).links();
      assertTrue(urls.contains("https://youtube.com/watch"));
    }

    @Test
    @DisplayName("extracts link preview url")
    void extractsLinkPreviewUrl() {
      LinkPreviewBlock lp = new LinkPreviewBlock();
      lp.getLinkPreview().setUrl("https://github.com");

      List<String> urls = NotionBlocksViewer.of(lp).links();
      assertTrue(urls.contains("https://github.com"));
    }

    @Test
    @DisplayName("extracts external image url")
    void extractsExternalImageUrl() {
      ImageBlock img = NotionBlocks.image("https://cdn.example.com/photo.jpg");

      List<String> urls = NotionBlocksViewer.of(img).links();
      assertTrue(urls.contains("https://cdn.example.com/photo.jpg"));
    }

    @Test
    @DisplayName("extracts multiple links across blocks")
    void extractsMultipleLinksAcrossBlocks() {
      RichText link1 = NotionText.url("https://one.com");
      RichText link2 = NotionText.url("https://two.com");
      ParagraphBlock p = ParagraphBlock.builder().text(link1).text(link2).build();
      BookmarkBlock bm = NotionBlocks.bookmark("https://three.com");

      List<String> urls = NotionBlocksViewer.of(p, bm).links();
      assertEquals(3, urls.size());
      assertTrue(urls.contains("https://one.com"));
      assertTrue(urls.contains("https://two.com"));
      assertTrue(urls.contains("https://three.com"));
    }

    @Test
    @DisplayName("no links returns empty list")
    void noLinks_returnsEmptyList() {
      NotionBlocksViewer view = NotionBlocksViewer.of(NotionBlocks.paragraph("plain text"));
      assertTrue(view.links().isEmpty());
    }

    @Test
    @DisplayName("empty view returns empty list")
    void emptyView_returnsEmptyList() {
      assertTrue(NotionBlocksViewer.of(new ArrayList<>()).links().isEmpty());
    }

    @Test
    @DisplayName("extracts links from direct child")
    void extractsLinksFromDirectChild() {
      RichText childLink = NotionText.url("https://child.example.com");
      ParagraphBlock parent =
          ParagraphBlock.builder()
              .text("parent")
              .children(c -> c.block(ParagraphBlock.builder().text(childLink).build()))
              .build();

      List<String> urls = NotionBlocksViewer.of(parent).links();
      assertTrue(urls.contains("https://child.example.com"));
    }

    @Test
    @DisplayName("extracts links from deeply nested child")
    void extractsLinksFromDeeplyNestedChild() {
      RichText deepLink = NotionText.url("https://deep.example.com");
      ParagraphBlock root =
          ParagraphBlock.builder()
              .text("level1")
              .children(
                  c ->
                      c.block(
                          ParagraphBlock.builder()
                              .text("level2")
                              .children(
                                  ch -> ch.block(ParagraphBlock.builder().text(deepLink).build()))
                              .build()))
              .build();

      List<String> urls = NotionBlocksViewer.of(root).links();
      assertTrue(urls.contains("https://deep.example.com"));
    }

    @Test
    @DisplayName("extracts links from parent and children")
    void extractsLinksFromParentAndChildren() {
      RichText parentLink = NotionText.url("https://parent.example.com");
      RichText childLink = NotionText.url("https://child.example.com");
      ParagraphBlock parent =
          ParagraphBlock.builder()
              .text(parentLink)
              .children(c -> c.block(ParagraphBlock.builder().text(childLink).build()))
              .build();

      List<String> urls = NotionBlocksViewer.of(parent).links();
      assertEquals(2, urls.size());
      assertEquals("https://parent.example.com", urls.get(0));
      assertEquals("https://child.example.com", urls.get(1));
    }

    @Test
    @DisplayName("extracts links from toggle children")
    void extractsLinksFromToggleChildren() {
      RichText childLink = NotionText.url("https://hidden.example.com");
      ToggleBlock toggle =
          ToggleBlock.builder()
              .text("toggle")
              .children(c -> c.block(ParagraphBlock.builder().text(childLink).build()))
              .build();

      List<String> urls = NotionBlocksViewer.of(toggle).links();
      assertTrue(urls.contains("https://hidden.example.com"));
    }

    @Test
    @DisplayName("no links in parent or children returns empty")
    void noLinksInParentOrChildren_returnsEmpty() {
      ParagraphBlock parent =
          ParagraphBlock.builder()
              .text("no links here")
              .children(c -> c.paragraph("also no links"))
              .build();

      assertTrue(NotionBlocksViewer.of(parent).links().isEmpty());
    }
  }

  @Nested
  class TextualFilter {

    @Test
    @DisplayName("includes paragraphs")
    void includesParagraphs() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(NotionBlocks.paragraph("p"), NotionBlocks.divider());
      assertEquals(1, view.textual().size());
      assertInstanceOf(ParagraphBlock.class, view.textual().first().orElseThrow());
    }

    @Test
    @DisplayName("includes all heading levels")
    void includesAllHeadingLevels() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.heading1("h1"),
              NotionBlocks.heading2("h2"),
              NotionBlocks.heading3("h3"),
              NotionBlocks.heading4("h4"),
              NotionBlocks.image("http://img.png"));

      assertEquals(4, view.textual().size());
    }

    @Test
    @DisplayName("includes bullets numbered todos toggles")
    void includesBulletsNumberedTodosToggles() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.bullet("b"),
              NotionBlocks.numbered("n"),
              NotionBlocks.todo("t"),
              NotionBlocks.toggle("tg"),
              NotionBlocks.divider());

      assertEquals(4, view.textual().size());
    }

    @Test
    @DisplayName("includes quotes and callouts")
    void includesQuotesAndCallouts() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.quote("q"),
              NotionBlocks.callout("c"),
              NotionBlocks.image("http://img.png"));

      assertEquals(2, view.textual().size());
    }

    @Test
    @DisplayName("excludes code blocks")
    void excludesCodeBlocks() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.paragraph("p"), NotionBlocks.code("java", "System.out.println();"));

      assertEquals(1, view.textual().size());
      assertInstanceOf(ParagraphBlock.class, view.textual().first().orElseThrow());
    }

    @Test
    @DisplayName("excludes media and structural blocks")
    void excludesMediaAndStructuralBlocks() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.image("http://img.png"),
              NotionBlocks.video("http://vid.mp4"),
              NotionBlocks.audio("http://audio.mp3"),
              NotionBlocks.bookmark("http://example.com"),
              NotionBlocks.divider(),
              NotionBlocks.embed("http://example.com"),
              NotionBlocks.paragraph("p"));

      assertEquals(1, view.textual().size());
    }

    @Test
    @DisplayName("empty view returns empty")
    void emptyView_returnsEmpty() {
      assertTrue(NotionBlocksViewer.of(new ArrayList<>()).textual().isEmpty());
    }

    @Test
    @DisplayName("all textual types returns all")
    void allTextualTypes_returnsAll() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.paragraph("p"),
              NotionBlocks.heading1("h1"),
              NotionBlocks.heading2("h2"),
              NotionBlocks.heading3("h3"),
              NotionBlocks.heading4("h4"),
              NotionBlocks.bullet("b"),
              NotionBlocks.numbered("n"),
              NotionBlocks.todo("t"),
              NotionBlocks.toggle("tg"),
              NotionBlocks.quote("q"),
              NotionBlocks.callout("c"));

      assertEquals(11, view.textual().size());
    }
  }

  @Nested
  class MediaFilter {

    @Test
    @DisplayName("includes image block")
    void includesImageBlock() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(NotionBlocks.image("http://img.png"), NotionBlocks.paragraph("p"));
      assertEquals(1, view.media().size());
      assertInstanceOf(ImageBlock.class, view.media().first().orElseThrow());
    }

    @Test
    @DisplayName("includes video block")
    void includesVideoBlock() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(NotionBlocks.video("http://vid.mp4"), NotionBlocks.paragraph("p"));
      assertEquals(1, view.media().size());
      assertInstanceOf(VideoBlock.class, view.media().first().orElseThrow());
    }

    @Test
    @DisplayName("includes audio block")
    void includesAudioBlock() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.audio("http://audio.mp3"), NotionBlocks.paragraph("p"));
      assertEquals(1, view.media().size());
      assertInstanceOf(AudioBlock.class, view.media().first().orElseThrow());
    }

    @Test
    @DisplayName("includes pdf block")
    void includesPdfBlock() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(NotionBlocks.pdf("http://doc.pdf"), NotionBlocks.paragraph("p"));
      assertEquals(1, view.media().size());
      assertInstanceOf(PdfBlock.class, view.media().first().orElseThrow());
    }

    @Test
    @DisplayName("includes file block with media extension")
    void includesFileBlockWithMediaExtension() {
      FileBlock imgFile = NotionBlocks.file("http://cdn.example.com/photo.jpg");
      FileBlock mp4File = NotionBlocks.file("http://cdn.example.com/clip.mp4");
      FileBlock mp3File = NotionBlocks.file("http://cdn.example.com/song.mp3");
      FileBlock pdfFile = NotionBlocks.file("http://cdn.example.com/doc.pdf");

      NotionBlocksViewer view = NotionBlocksViewer.of(imgFile, mp4File, mp3File, pdfFile);
      assertEquals(4, view.media().size());
    }

    @Test
    @DisplayName("excludes file block with non media extension")
    void excludesFileBlockWithNonMediaExtension() {
      FileBlock txtFile = NotionBlocks.file("http://cdn.example.com/readme.txt");
      FileBlock zipFile = NotionBlocks.file("http://cdn.example.com/archive.zip");
      FileBlock docFile = NotionBlocks.file("http://cdn.example.com/report.docx");

      NotionBlocksViewer view = NotionBlocksViewer.of(txtFile, zipFile, docFile);
      assertTrue(view.media().isEmpty());
    }

    @Test
    @DisplayName("excludes file block with no url")
    void excludesFileBlockWithNoUrl() {
      FileBlock fb = new FileBlock();
      NotionBlocksViewer view = NotionBlocksViewer.of(fb);
      assertTrue(view.media().isEmpty());
    }

    @Test
    @DisplayName("file block extension check ignores query params")
    void fileBlockExtensionCheckIgnoresQueryParams() {
      FileBlock fb = NotionBlocks.file("http://cdn.example.com/photo.png?token=abc123");

      assertEquals(1, NotionBlocksViewer.of(fb).media().size());
    }

    @Test
    @DisplayName("file block extension check is case insensitive")
    void fileBlockExtensionCheckIsCaseInsensitive() {
      FileBlock fb = NotionBlocks.file("http://cdn.example.com/photo.JPEG");

      assertEquals(1, NotionBlocksViewer.of(fb).media().size());
    }

    @Test
    @DisplayName("excludes textual and structural blocks")
    void excludesTextualAndStructuralBlocks() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.paragraph("p"),
              NotionBlocks.heading1("h"),
              NotionBlocks.bullet("b"),
              NotionBlocks.todo("t"),
              NotionBlocks.divider(),
              NotionBlocks.bookmark("http://example.com"),
              NotionBlocks.code("java", "code"));

      assertTrue(view.media().isEmpty());
    }

    @Test
    @DisplayName("all media types returns all")
    void allMediaTypes_returnsAll() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.image("http://i.png"),
              NotionBlocks.video("http://v.mp4"),
              NotionBlocks.audio("http://a.mp3"),
              NotionBlocks.pdf("http://d.pdf"),
              NotionBlocks.file("http://x.webp"));

      assertEquals(5, view.media().size());
    }

    @Test
    @DisplayName("empty view returns empty")
    void emptyView_returnsEmpty() {
      assertTrue(NotionBlocksViewer.of(new ArrayList<>()).media().isEmpty());
    }

    @Test
    @DisplayName("mixed media and textual filters correctly")
    void mixedMediaAndTextual_filtersCorrectly() {
      NotionBlocksViewer view =
          NotionBlocksViewer.of(
              NotionBlocks.image("http://i.png"),
              NotionBlocks.paragraph("text"),
              NotionBlocks.video("http://v.mp4"),
              NotionBlocks.heading1("heading"),
              NotionBlocks.audio("http://a.mp3"));

      assertEquals(3, view.media().size());
      assertEquals(2, view.textual().size());
    }
  }
}
