package io.kristixlab.notion.api.model.helper;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.block.*;
import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class BlockListViewerTest {

  @Nested
  class Factories {

    @Test
    void ofNullList_returnsEmptyView() {
      BlockListViewer view = BlockListViewer.of((List<Block>) null);
      assertTrue(view.isEmpty());
      assertEquals(0, view.size());
    }

    @Test
    void ofEmptyList_returnsEmptyView() {
      BlockListViewer view = BlockListViewer.of(new ArrayList<>());
      assertTrue(view.isEmpty());
    }

    @Test
    void ofList_copiesDefensively() {
      List<Block> source = new ArrayList<>();
      source.add(NotionBlocks.paragraph("a"));
      BlockListViewer view = BlockListViewer.of(source);

      source.add(NotionBlocks.paragraph("b"));
      assertEquals(1, view.size());
    }

    @Test
    void ofVarargs_wrapsBlocks() {
      BlockListViewer view =
          BlockListViewer.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));
      assertEquals(2, view.size());
    }

    @Test
    void ofNullVarargs_returnsEmptyView() {
      BlockListViewer view = BlockListViewer.of((Block[]) null);
      assertTrue(view.isEmpty());
    }

    @Test
    void ofEmptyVarargs_returnsEmptyView() {
      BlockListViewer view = BlockListViewer.of(new Block[0]);
      assertTrue(view.isEmpty());
    }
  }

  @Nested
  class TypeFiltering {

    @Test
    void ofType_filtersByExactClass() {
      BlockListViewer view =
          BlockListViewer.of(
              NotionBlocks.paragraph("p"), NotionBlocks.heading1("h1"), NotionBlocks.todo("td"));

      BlockListViewer paragraphs = view.ofType(ParagraphBlock.class);
      assertEquals(1, paragraphs.size());
      assertInstanceOf(ParagraphBlock.class, paragraphs.first().orElseThrow());
    }

    @Test
    void paragraphs_returnsOnlyParagraphs() {
      BlockListViewer view =
          BlockListViewer.of(
              NotionBlocks.paragraph("p1"),
              NotionBlocks.heading1("h"),
              NotionBlocks.paragraph("p2"));

      assertEquals(2, view.paragraphs().size());
    }

    @Test
    void headings_returnsAllHeadingLevels() {
      BlockListViewer view =
          BlockListViewer.of(
              NotionBlocks.heading1("h1"),
              NotionBlocks.heading2("h2"),
              NotionBlocks.heading3("h3"),
              NotionBlocks.heading4("h4"),
              NotionBlocks.paragraph("p"));

      assertEquals(4, view.headings().size());
    }

    @Test
    void todos_returnsOnlyToDoBlocks() {
      BlockListViewer view =
          BlockListViewer.of(
              NotionBlocks.todo("t1"), NotionBlocks.todo("t2"), NotionBlocks.bullet("b"));

      assertEquals(2, view.todos().size());
    }

    @Test
    void bullets_returnsOnlyBulletedListItems() {
      BlockListViewer view =
          BlockListViewer.of(NotionBlocks.bullet("b"), NotionBlocks.paragraph("p"));

      assertEquals(1, view.bullets().size());
      assertInstanceOf(BulletedListItemBlock.class, view.bullets().first().orElseThrow());
    }

    @Test
    void numbered_returnsOnlyNumberedListItems() {
      BlockListViewer view =
          BlockListViewer.of(NotionBlocks.numbered("n"), NotionBlocks.paragraph("p"));

      assertEquals(1, view.numbered().size());
    }

    @Test
    void toggles_returnsOnlyToggleBlocks() {
      BlockListViewer view =
          BlockListViewer.of(NotionBlocks.toggle("t"), NotionBlocks.paragraph("p"));

      assertEquals(1, view.toggles().size());
    }

    @Test
    void quotes_returnsOnlyQuoteBlocks() {
      BlockListViewer view =
          BlockListViewer.of(NotionBlocks.quote("q"), NotionBlocks.paragraph("p"));

      assertEquals(1, view.quotes().size());
    }

    @Test
    void callouts_returnsOnlyCalloutBlocks() {
      BlockListViewer view =
          BlockListViewer.of(NotionBlocks.callout("c"), NotionBlocks.paragraph("p"));

      assertEquals(1, view.callouts().size());
    }

    @Test
    void code_returnsOnlyCodeBlocks() {
      BlockListViewer view =
          BlockListViewer.of(NotionBlocks.code("java", "x"), NotionBlocks.paragraph("p"));

      assertEquals(1, view.code().size());
    }

    @Test
    void images_returnsOnlyImageBlocks() {
      BlockListViewer view =
          BlockListViewer.of(
              NotionBlocks.image("https://example.com/img.png"), NotionBlocks.paragraph("p"));

      assertEquals(1, view.images().size());
    }

    @Test
    void filterOnEmptyView_returnsEmptyView() {
      BlockListViewer view = BlockListViewer.of(new ArrayList<>());
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

      BlockListViewer view = BlockListViewer.of(checkedTodo, uncheckedTodo);

      assertEquals(1, view.checked().size());
      assertEquals("done", view.checked().plainText());
    }

    @Test
    void unchecked_returnsOnlyUncheckedToDos() {
      ToDoBlock checkedTodo = ToDoBlock.builder().text("done").checked(true).build();
      ToDoBlock uncheckedTodo = ToDoBlock.builder().text("pending").checked(false).build();

      BlockListViewer view = BlockListViewer.of(checkedTodo, uncheckedTodo);

      assertEquals(1, view.unchecked().size());
      assertEquals("pending", view.unchecked().plainText());
    }

    @Test
    void unchecked_treatsNullCheckedAsUnchecked() {
      ToDoBlock todo = ToDoBlock.builder().text("no state").build();

      assertEquals(1, BlockListViewer.of(todo).unchecked().size());
    }

    @Test
    void checked_silentlyFiltersNonToDoBlocks() {
      BlockListViewer view =
          BlockListViewer.of(NotionBlocks.paragraph("p"), NotionBlocks.todo("t"));

      assertEquals(0, view.checked().size());
    }

    @Test
    void unchecked_silentlyFiltersNonToDoBlocks() {
      BlockListViewer view = BlockListViewer.of(NotionBlocks.paragraph("p"));

      assertTrue(view.unchecked().isEmpty());
    }

    @Test
    void chainingTodosAndChecked() {
      ToDoBlock checked = ToDoBlock.builder().text("done").checked(true).build();
      ToDoBlock unchecked = ToDoBlock.builder().text("pending").build();

      BlockListViewer view = BlockListViewer.of(checked, unchecked, NotionBlocks.paragraph("p"));

      assertEquals(1, view.todos().checked().size());
      assertEquals(1, view.todos().unchecked().size());
    }
  }

  @Nested
  class Where {

    @Test
    void filtersWithCustomPredicate() {
      BlockListViewer view =
          BlockListViewer.of(
              NotionBlocks.paragraph("short"), NotionBlocks.paragraph("a longer paragraph text"));

      BlockListViewer long_ =
          view.where(
              b ->
                  b instanceof ParagraphBlock pb
                      && pb.getParagraph().getRichText().get(0).getPlainText().length() > 10);

      assertEquals(1, long_.size());
    }

    @Test
    void whereNoMatch_returnsEmptyView() {
      BlockListViewer view = BlockListViewer.of(NotionBlocks.paragraph("p"));
      assertTrue(view.where(b -> false).isEmpty());
    }
  }

  @Nested
  class PlainTextExtraction {

    @Test
    void plainText_fromParagraphs() {
      BlockListViewer view =
          BlockListViewer.of(NotionBlocks.paragraph("Hello"), NotionBlocks.paragraph("World"));

      assertEquals("Hello\nWorld", view.plainText());
    }

    @Test
    void plainText_withCustomDelimiter() {
      BlockListViewer view =
          BlockListViewer.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));

      assertEquals("a, b", view.plainText(", "));
    }

    @Test
    void plainText_fromHeadings() {
      BlockListViewer view =
          BlockListViewer.of(
              NotionBlocks.heading1("H1"),
              NotionBlocks.heading2("H2"),
              NotionBlocks.heading3("H3"),
              NotionBlocks.heading4("H4"));

      assertEquals("H1\nH2\nH3\nH4", view.plainText());
    }

    @Test
    void plainText_fromToggle() {
      assertEquals(
          "toggle text", BlockListViewer.of(NotionBlocks.toggle("toggle text")).plainText());
    }

    @Test
    void plainText_fromQuote() {
      assertEquals("quoted", BlockListViewer.of(NotionBlocks.quote("quoted")).plainText());
    }

    @Test
    void plainText_fromCallout() {
      assertEquals("callout", BlockListViewer.of(NotionBlocks.callout("callout")).plainText());
    }

    @Test
    void plainText_fromBullet() {
      assertEquals("item", BlockListViewer.of(NotionBlocks.bullet("item")).plainText());
    }

    @Test
    void plainText_fromNumbered() {
      assertEquals("item", BlockListViewer.of(NotionBlocks.numbered("item")).plainText());
    }

    @Test
    void plainText_fromToDo() {
      assertEquals("task", BlockListViewer.of(NotionBlocks.todo("task")).plainText());
    }

    @Test
    void plainText_fromCode() {
      assertEquals(
          "int x = 5;", BlockListViewer.of(NotionBlocks.code("java", "int x = 5;")).plainText());
    }

    @Test
    void plainText_fromChildPage() {
      ChildPageBlock cpb = new ChildPageBlock();
      cpb.getChildPage().setTitle("My Page");
      assertEquals("My Page", BlockListViewer.of(cpb).plainText());
    }

    @Test
    void plainText_fromChildDatabase() {
      ChildDatabaseBlock cdb = new ChildDatabaseBlock();
      cdb.getChildDatabase().setTitle("My DB");
      assertEquals("My DB", BlockListViewer.of(cdb).plainText());
    }

    @Test
    void plainText_fromEquation() {
      EquationBlock eb = new EquationBlock();
      eb.getEquation().setExpression("a^2 + b^2 = c^2");
      assertEquals("a^2 + b^2 = c^2", BlockListViewer.of(eb).plainText());
    }

    @Test
    void plainText_skipsNonTextualBlocks() {
      BlockListViewer view =
          BlockListViewer.of(
              NotionBlocks.paragraph("text"),
              NotionBlocks.divider(),
              NotionBlocks.image("https://example.com/img.png"),
              NotionBlocks.paragraph("more text"));

      assertEquals("text\nmore text", view.plainText());
    }

    @Test
    void plainText_onEmptyView_returnsEmptyString() {
      assertEquals("", BlockListViewer.of(new ArrayList<>()).plainText());
    }

    @Test
    void plainTextList_returnsOneEntryPerBlock() {
      BlockListViewer view =
          BlockListViewer.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));

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

      assertEquals("bold normal", BlockListViewer.of(p).plainText());
    }
  }

  @Nested
  class Navigation {

    @Test
    void first_returnsFirstBlock() {
      ParagraphBlock p = NotionBlocks.paragraph("first");
      BlockListViewer view = BlockListViewer.of(p, NotionBlocks.paragraph("second"));

      assertSame(p, view.first().orElseThrow());
    }

    @Test
    void first_onEmptyView_returnsEmpty() {
      assertTrue(BlockListViewer.of(new ArrayList<>()).first().isEmpty());
    }

    @Test
    void firstWithType_returnsFirstMatchingBlock() {
      BlockListViewer view =
          BlockListViewer.of(
              NotionBlocks.paragraph("p"), NotionBlocks.heading1("h"), NotionBlocks.todo("t"));

      Optional<HeadingOneBlock> heading = view.first(HeadingOneBlock.class);
      assertTrue(heading.isPresent());
      assertEquals("h", heading.get().getHeading1().getRichText().get(0).getPlainText());
    }

    @Test
    void firstWithType_noMatch_returnsEmpty() {
      BlockListViewer view = BlockListViewer.of(NotionBlocks.paragraph("p"));

      assertTrue(view.first(ToDoBlock.class).isEmpty());
    }

    @Test
    void firstWithPredicate_returnsFirstMatch() {
      BlockListViewer view =
          BlockListViewer.of(NotionBlocks.paragraph("short"), NotionBlocks.paragraph("a long one"));

      Optional<Block> result =
          view.first(
              b ->
                  b instanceof ParagraphBlock pb
                      && pb.getParagraph().getRichText().get(0).getPlainText().length() > 5);

      assertTrue(result.isPresent());
    }

    @Test
    void firstWithPredicate_noMatch_returnsEmpty() {
      BlockListViewer view = BlockListViewer.of(NotionBlocks.paragraph("p"));
      assertTrue(view.first(b -> false).isEmpty());
    }

    @Test
    void last_returnsLastBlock() {
      ParagraphBlock last = NotionBlocks.paragraph("last");
      BlockListViewer view = BlockListViewer.of(NotionBlocks.paragraph("first"), last);

      assertSame(last, view.last().orElseThrow());
    }

    @Test
    void last_onEmptyView_returnsEmpty() {
      assertTrue(BlockListViewer.of(new ArrayList<>()).last().isEmpty());
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

      BlockListViewer flat = BlockListViewer.of(parent).flatten();

      assertEquals(3, flat.size());
      assertEquals("parent", flat.paragraphs().plainTextList().get(0));
      assertEquals("child1", flat.paragraphs().plainTextList().get(1));
      assertEquals("child2", flat.paragraphs().plainTextList().get(2));
    }

    @Test
    void flatten_depthFirst_parentBeforeChildren() {
      ParagraphBlock parent =
          ParagraphBlock.builder().text("parent").children(c -> c.paragraph("child")).build();

      BlockListViewer flat = BlockListViewer.of(parent).flatten();
      List<String> texts = flat.plainTextList();

      assertEquals("parent", texts.get(0));
      assertEquals("child", texts.get(1));
    }

    @Test
    void flatten_toggle_includesNestedContent() {
      ToggleBlock toggle =
          ToggleBlock.builder().text("toggle").children(c -> c.paragraph("inside")).build();

      BlockListViewer flat = BlockListViewer.of(toggle).flatten();
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

      BlockListViewer flat = BlockListViewer.of(deep).flatten();
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

      BlockListViewer flat = BlockListViewer.of(colList).flatten();
      assertEquals(5, flat.size());
      assertEquals(2, flat.paragraphs().size());
      assertEquals("col1-text\ncol2-text", flat.paragraphs().plainText());
    }

    @Test
    void flatten_synced_traversesChildren() {
      SyncedBlock synced = new SyncedBlock();
      synced.getSyncedBlock().setChildren(List.of(NotionBlocks.paragraph("synced-child")));

      BlockListViewer flat = BlockListViewer.of(synced).flatten();
      assertEquals(2, flat.size());
      assertEquals("synced-child", flat.paragraphs().plainText());
    }

    @Test
    void flatten_blockWithNoChildren_returnsSingleBlock() {
      BlockListViewer flat = BlockListViewer.of(NotionBlocks.paragraph("single")).flatten();
      assertEquals(1, flat.size());
    }

    @Test
    void flatten_emptyView_returnsEmptyView() {
      assertTrue(BlockListViewer.of(new ArrayList<>()).flatten().isEmpty());
    }
  }

  @Nested
  class TypedAccess {

    @Test
    void as_castsAllBlocksToType() {
      BlockListViewer view =
          BlockListViewer.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));

      List<ParagraphBlock> paragraphs = view.paragraphs().as(ParagraphBlock.class);
      assertEquals(2, paragraphs.size());
      assertInstanceOf(ParagraphBlock.class, paragraphs.get(0));
    }

    @Test
    void as_throwsOnTypeMismatch() {
      BlockListViewer view = BlockListViewer.of(NotionBlocks.paragraph("p"));

      assertThrows(ClassCastException.class, () -> view.as(ToDoBlock.class));
    }

    @Test
    void blocks_returnsUnmodifiableList() {
      BlockListViewer view = BlockListViewer.of(NotionBlocks.paragraph("p"));
      assertThrows(
          UnsupportedOperationException.class, () -> view.blocks().add(new ParagraphBlock()));
    }

    @Test
    void stream_enablesAdvancedOperations() {
      BlockListViewer view =
          BlockListViewer.of(
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
          BlockListViewer.of(
                  NotionBlocks.paragraph("a"),
                  NotionBlocks.paragraph("b"),
                  NotionBlocks.paragraph("c"))
              .size());
    }

    @Test
    void isEmpty_trueForEmptyView() {
      assertTrue(BlockListViewer.of(new ArrayList<>()).isEmpty());
    }

    @Test
    void isEmpty_falseForNonEmptyView() {
      assertFalse(BlockListViewer.of(NotionBlocks.paragraph("p")).isEmpty());
    }

    @Test
    void forEach_iteratesAllBlocks() {
      BlockListViewer view =
          BlockListViewer.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));

      List<Block> visited = new ArrayList<>();
      view.forEach(visited::add);
      assertEquals(2, visited.size());
    }

    @Test
    void forEachTyped_iteratesOnlyMatchingBlocks() {
      BlockListViewer view =
          BlockListViewer.of(
              NotionBlocks.paragraph("p"), NotionBlocks.heading1("h"), NotionBlocks.todo("t"));

      List<ParagraphBlock> paragraphs = new ArrayList<>();
      view.forEach(ParagraphBlock.class, paragraphs::add);

      assertEquals(1, paragraphs.size());
    }

    @Test
    void iterator_worksWithEnhancedForLoop() {
      BlockListViewer view =
          BlockListViewer.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));

      int count = 0;
      for (Block b : view) {
        assertInstanceOf(ParagraphBlock.class, b);
        count++;
      }
      assertEquals(2, count);
    }

    @Test
    void iterator_isUnmodifiable() {
      BlockListViewer view = BlockListViewer.of(NotionBlocks.paragraph("a"));
      var it = view.iterator();
      it.next();
      assertThrows(UnsupportedOperationException.class, it::remove);
    }
  }

  @Nested
  class ChainingWorkflows {

    @Test
    void filterThenExtractText() {
      BlockListViewer view =
          BlockListViewer.of(
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

      BlockListViewer view = BlockListViewer.of(toggle, NotionBlocks.paragraph("top-level"));

      String allParaText = view.flatten().paragraphs().plainText();
      assertEquals("nested paragraph\ntop-level", allParaText);
    }

    @Test
    void todosCheckedUncheckedPlainText() {
      ToDoBlock done = ToDoBlock.builder().text("completed").checked(true).build();
      ToDoBlock pending = ToDoBlock.builder().text("pending").build();

      BlockListViewer view = BlockListViewer.of(done, pending);

      assertEquals("completed", view.todos().checked().plainText());
      assertEquals("pending", view.todos().unchecked().plainText());
    }
  }

  @Nested
  class Containing {

    @Test
    void matchesByPlainTextContent() {
      BlockListViewer view =
          BlockListViewer.of(
              NotionBlocks.paragraph("Hello world"),
              NotionBlocks.paragraph("Goodbye"),
              NotionBlocks.heading1("Hello heading"));

      BlockListViewer result = view.containing("hello");
      assertEquals(2, result.size());
    }

    @Test
    void matchesAcrossMultipleRichTextSegments() {
      RichText part1 = NotionText.plainText("Hel");
      RichText part2 = NotionText.plainText("lo world");
      ParagraphBlock p = ParagraphBlock.builder().text(part1).text(part2).build();

      BlockListViewer view = BlockListViewer.of(p, NotionBlocks.paragraph("other"));
      assertEquals(1, view.containing("hello").size());
    }

    @Test
    void matchesByRichTextHref() {
      RichText link = NotionText.url("https://example.com/page");
      ParagraphBlock p = ParagraphBlock.builder().text(link).build();

      BlockListViewer view = BlockListViewer.of(p);
      assertEquals(1, view.containing("example.com").size());
    }

    @Test
    void matchesByBookmarkUrl() {
      BookmarkBlock bookmark = NotionBlocks.bookmark("https://notion.so/docs");

      assertEquals(1, BlockListViewer.of(bookmark).containing("notion.so").size());
    }

    @Test
    void matchesByEmbedUrl() {
      EmbedBlock embed = NotionBlocks.embed("https://youtube.com/watch?v=123");

      assertEquals(1, BlockListViewer.of(embed).containing("youtube").size());
    }

    @Test
    void matchesByLinkPreviewUrl() {
      LinkPreviewBlock lp = new LinkPreviewBlock();
      lp.getLinkPreview().setUrl("https://github.com/repo");

      assertEquals(1, BlockListViewer.of(lp).containing("github").size());
    }

    @Test
    void matchesByChildPageTitle() {
      ChildPageBlock cpb = new ChildPageBlock();
      cpb.getChildPage().setTitle("Project Roadmap");

      assertEquals(1, BlockListViewer.of(cpb).containing("roadmap").size());
    }

    @Test
    void matchesByEquationExpression() {
      EquationBlock eb = new EquationBlock();
      eb.getEquation().setExpression("E = mc^2");

      assertEquals(1, BlockListViewer.of(eb).containing("mc^2").size());
    }

    @Test
    void noMatch_returnsEmpty() {
      BlockListViewer view = BlockListViewer.of(NotionBlocks.paragraph("Hello"));
      assertTrue(view.containing("xyz").isEmpty());
    }

    @Test
    void caseInsensitive() {
      BlockListViewer view = BlockListViewer.of(NotionBlocks.paragraph("Hello WORLD"));
      assertEquals(1, view.containing("hello world").size());
    }

    @Test
    void matchesByImageUrl() {
      ImageBlock img = NotionBlocks.image("https://cdn.example.com/photo.jpg");
      assertEquals(1, BlockListViewer.of(img).containing("cdn.example.com").size());
    }

    @Test
    void nullKeyword_throwsNpe() {
      BlockListViewer view = BlockListViewer.of(NotionBlocks.paragraph("text"));
      assertThrows(NullPointerException.class, () -> view.containing(null));
    }

    @Test
    void matchesInDirectChild() {
      ParagraphBlock parent =
          ParagraphBlock.builder()
              .text("parent text")
              .children(c -> c.paragraph("child keyword here"))
              .build();

      assertEquals(1, BlockListViewer.of(parent).containing("keyword").size());
    }

    @Test
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

      assertEquals(1, BlockListViewer.of(root).containing("deep keyword").size());
    }

    @Test
    void parentMatchShortCircuitsBeforeCheckingChildren() {
      ParagraphBlock parent =
          ParagraphBlock.builder()
              .text("keyword in parent")
              .children(c -> c.paragraph("child text"))
              .build();

      assertEquals(1, BlockListViewer.of(parent).containing("keyword").size());
    }

    @Test
    void noMatchInParentOrChildren_returnsEmpty() {
      ParagraphBlock parent =
          ParagraphBlock.builder().text("hello").children(c -> c.paragraph("world")).build();

      assertTrue(BlockListViewer.of(parent).containing("xyz").isEmpty());
    }

    @Test
    void matchesInToggleChildBlock() {
      ToggleBlock toggle =
          ToggleBlock.builder()
              .text("toggle label")
              .children(c -> c.paragraph("hidden keyword"))
              .build();

      assertEquals(1, BlockListViewer.of(toggle).containing("hidden keyword").size());
    }
  }

  @Nested
  class Links {

    @Test
    void extractsHrefsFromRichText() {
      RichText link = NotionText.url("https://example.com");
      ParagraphBlock p = ParagraphBlock.builder().text(link).build();

      List<String> urls = BlockListViewer.of(p).links();
      assertEquals(1, urls.size());
      assertEquals("https://example.com", urls.get(0));
    }

    @Test
    void extractsBookmarkUrl() {
      BookmarkBlock bookmark = NotionBlocks.bookmark("https://notion.so");

      List<String> urls = BlockListViewer.of(bookmark).links();
      assertTrue(urls.contains("https://notion.so"));
    }

    @Test
    void extractsEmbedUrl() {
      EmbedBlock embed = NotionBlocks.embed("https://youtube.com/watch");

      List<String> urls = BlockListViewer.of(embed).links();
      assertTrue(urls.contains("https://youtube.com/watch"));
    }

    @Test
    void extractsLinkPreviewUrl() {
      LinkPreviewBlock lp = new LinkPreviewBlock();
      lp.getLinkPreview().setUrl("https://github.com");

      List<String> urls = BlockListViewer.of(lp).links();
      assertTrue(urls.contains("https://github.com"));
    }

    @Test
    void extractsExternalImageUrl() {
      ImageBlock img = NotionBlocks.image("https://cdn.example.com/photo.jpg");

      List<String> urls = BlockListViewer.of(img).links();
      assertTrue(urls.contains("https://cdn.example.com/photo.jpg"));
    }

    @Test
    void extractsMultipleLinksAcrossBlocks() {
      RichText link1 = NotionText.url("https://one.com");
      RichText link2 = NotionText.url("https://two.com");
      ParagraphBlock p = ParagraphBlock.builder().text(link1).text(link2).build();
      BookmarkBlock bm = NotionBlocks.bookmark("https://three.com");

      List<String> urls = BlockListViewer.of(p, bm).links();
      assertEquals(3, urls.size());
      assertTrue(urls.contains("https://one.com"));
      assertTrue(urls.contains("https://two.com"));
      assertTrue(urls.contains("https://three.com"));
    }

    @Test
    void noLinks_returnsEmptyList() {
      BlockListViewer view = BlockListViewer.of(NotionBlocks.paragraph("plain text"));
      assertTrue(view.links().isEmpty());
    }

    @Test
    void emptyView_returnsEmptyList() {
      assertTrue(BlockListViewer.of(new ArrayList<>()).links().isEmpty());
    }

    @Test
    void extractsLinksFromDirectChild() {
      RichText childLink = NotionText.url("https://child.example.com");
      ParagraphBlock parent =
          ParagraphBlock.builder()
              .text("parent")
              .children(c -> c.block(ParagraphBlock.builder().text(childLink).build()))
              .build();

      List<String> urls = BlockListViewer.of(parent).links();
      assertTrue(urls.contains("https://child.example.com"));
    }

    @Test
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

      List<String> urls = BlockListViewer.of(root).links();
      assertTrue(urls.contains("https://deep.example.com"));
    }

    @Test
    void extractsLinksFromParentAndChildren() {
      RichText parentLink = NotionText.url("https://parent.example.com");
      RichText childLink = NotionText.url("https://child.example.com");
      ParagraphBlock parent =
          ParagraphBlock.builder()
              .text(parentLink)
              .children(c -> c.block(ParagraphBlock.builder().text(childLink).build()))
              .build();

      List<String> urls = BlockListViewer.of(parent).links();
      assertEquals(2, urls.size());
      assertEquals("https://parent.example.com", urls.get(0));
      assertEquals("https://child.example.com", urls.get(1));
    }

    @Test
    void extractsLinksFromToggleChildren() {
      RichText childLink = NotionText.url("https://hidden.example.com");
      ToggleBlock toggle =
          ToggleBlock.builder()
              .text("toggle")
              .children(c -> c.block(ParagraphBlock.builder().text(childLink).build()))
              .build();

      List<String> urls = BlockListViewer.of(toggle).links();
      assertTrue(urls.contains("https://hidden.example.com"));
    }

    @Test
    void noLinksInParentOrChildren_returnsEmpty() {
      ParagraphBlock parent =
          ParagraphBlock.builder()
              .text("no links here")
              .children(c -> c.paragraph("also no links"))
              .build();

      assertTrue(BlockListViewer.of(parent).links().isEmpty());
    }
  }

  @Nested
  class TextualFilter {

    @Test
    void includesParagraphs() {
      BlockListViewer view =
          BlockListViewer.of(NotionBlocks.paragraph("p"), NotionBlocks.divider());
      assertEquals(1, view.textual().size());
      assertInstanceOf(ParagraphBlock.class, view.textual().first().orElseThrow());
    }

    @Test
    void includesAllHeadingLevels() {
      BlockListViewer view =
          BlockListViewer.of(
              NotionBlocks.heading1("h1"),
              NotionBlocks.heading2("h2"),
              NotionBlocks.heading3("h3"),
              NotionBlocks.heading4("h4"),
              NotionBlocks.image("http://img.png"));

      assertEquals(4, view.textual().size());
    }

    @Test
    void includesBulletsNumberedTodosToggles() {
      BlockListViewer view =
          BlockListViewer.of(
              NotionBlocks.bullet("b"),
              NotionBlocks.numbered("n"),
              NotionBlocks.todo("t"),
              NotionBlocks.toggle("tg"),
              NotionBlocks.divider());

      assertEquals(4, view.textual().size());
    }

    @Test
    void includesQuotesAndCallouts() {
      BlockListViewer view =
          BlockListViewer.of(
              NotionBlocks.quote("q"),
              NotionBlocks.callout("c"),
              NotionBlocks.image("http://img.png"));

      assertEquals(2, view.textual().size());
    }

    @Test
    void excludesCodeBlocks() {
      BlockListViewer view =
          BlockListViewer.of(
              NotionBlocks.paragraph("p"), NotionBlocks.code("java", "System.out.println();"));

      assertEquals(1, view.textual().size());
      assertInstanceOf(ParagraphBlock.class, view.textual().first().orElseThrow());
    }

    @Test
    void excludesMediaAndStructuralBlocks() {
      BlockListViewer view =
          BlockListViewer.of(
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
    void emptyView_returnsEmpty() {
      assertTrue(BlockListViewer.of(new ArrayList<>()).textual().isEmpty());
    }

    @Test
    void allTextualTypes_returnsAll() {
      BlockListViewer view =
          BlockListViewer.of(
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
    void includesImageBlock() {
      BlockListViewer view =
          BlockListViewer.of(NotionBlocks.image("http://img.png"), NotionBlocks.paragraph("p"));
      assertEquals(1, view.media().size());
      assertInstanceOf(ImageBlock.class, view.media().first().orElseThrow());
    }

    @Test
    void includesVideoBlock() {
      BlockListViewer view =
          BlockListViewer.of(NotionBlocks.video("http://vid.mp4"), NotionBlocks.paragraph("p"));
      assertEquals(1, view.media().size());
      assertInstanceOf(VideoBlock.class, view.media().first().orElseThrow());
    }

    @Test
    void includesAudioBlock() {
      BlockListViewer view =
          BlockListViewer.of(NotionBlocks.audio("http://audio.mp3"), NotionBlocks.paragraph("p"));
      assertEquals(1, view.media().size());
      assertInstanceOf(AudioBlock.class, view.media().first().orElseThrow());
    }

    @Test
    void includesPdfBlock() {
      BlockListViewer view =
          BlockListViewer.of(NotionBlocks.pdf("http://doc.pdf"), NotionBlocks.paragraph("p"));
      assertEquals(1, view.media().size());
      assertInstanceOf(PdfBlock.class, view.media().first().orElseThrow());
    }

    @Test
    void includesFileBlockWithMediaExtension() {
      FileBlock imgFile = NotionBlocks.file("http://cdn.example.com/photo.jpg");
      FileBlock mp4File = NotionBlocks.file("http://cdn.example.com/clip.mp4");
      FileBlock mp3File = NotionBlocks.file("http://cdn.example.com/song.mp3");
      FileBlock pdfFile = NotionBlocks.file("http://cdn.example.com/doc.pdf");

      BlockListViewer view = BlockListViewer.of(imgFile, mp4File, mp3File, pdfFile);
      assertEquals(4, view.media().size());
    }

    @Test
    void excludesFileBlockWithNonMediaExtension() {
      FileBlock txtFile = NotionBlocks.file("http://cdn.example.com/readme.txt");
      FileBlock zipFile = NotionBlocks.file("http://cdn.example.com/archive.zip");
      FileBlock docFile = NotionBlocks.file("http://cdn.example.com/report.docx");

      BlockListViewer view = BlockListViewer.of(txtFile, zipFile, docFile);
      assertTrue(view.media().isEmpty());
    }

    @Test
    void excludesFileBlockWithNoUrl() {
      FileBlock fb = new FileBlock();
      BlockListViewer view = BlockListViewer.of(fb);
      assertTrue(view.media().isEmpty());
    }

    @Test
    void fileBlockExtensionCheckIgnoresQueryParams() {
      FileBlock fb = NotionBlocks.file("http://cdn.example.com/photo.png?token=abc123");

      assertEquals(1, BlockListViewer.of(fb).media().size());
    }

    @Test
    void fileBlockExtensionCheckIsCaseInsensitive() {
      FileBlock fb = NotionBlocks.file("http://cdn.example.com/photo.JPEG");

      assertEquals(1, BlockListViewer.of(fb).media().size());
    }

    @Test
    void excludesTextualAndStructuralBlocks() {
      BlockListViewer view =
          BlockListViewer.of(
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
    void allMediaTypes_returnsAll() {
      BlockListViewer view =
          BlockListViewer.of(
              NotionBlocks.image("http://i.png"),
              NotionBlocks.video("http://v.mp4"),
              NotionBlocks.audio("http://a.mp3"),
              NotionBlocks.pdf("http://d.pdf"),
              NotionBlocks.file("http://x.webp"));

      assertEquals(5, view.media().size());
    }

    @Test
    void emptyView_returnsEmpty() {
      assertTrue(BlockListViewer.of(new ArrayList<>()).media().isEmpty());
    }

    @Test
    void mixedMediaAndTextual_filtersCorrectly() {
      BlockListViewer view =
          BlockListViewer.of(
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
