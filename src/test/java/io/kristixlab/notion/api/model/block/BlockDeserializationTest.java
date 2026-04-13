package io.kristixlab.notion.api.model.block;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.base.json.JacksonSerializer;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

class BlockDeserializationTest {

  private static final JacksonSerializer JSON = JacksonSerializer.withDefaults();

  @Test
  void deserialize_retrieveBlocksFixture_mapsTextualAndStructuralBlockTypes() {
    BlockList blockList = deserializeFixture("json/retrieve-blocks-rs.json");

    assertNotNull(blockList);
    assertNotNull(blockList.getResults());
    assertFalse(blockList.getResults().isEmpty());

    List<Block> results = blockList.getResults();
    assertContainsBlockType(results, TableOfContentsBlock.class);
    assertContainsBlockType(results, HeadingOneBlock.class);
    assertContainsBlockType(results, HeadingTwoBlock.class);
    assertContainsBlockType(results, HeadingThreeBlock.class);
    assertContainsBlockType(results, ParagraphBlock.class);
    assertContainsBlockType(results, DividerBlock.class);
    assertContainsBlockType(results, CalloutBlock.class);
    assertContainsBlockType(results, QuoteBlock.class);
    assertContainsBlockType(results, ChildPageBlock.class);
    assertContainsBlockType(results, BulletedListItemBlock.class);
    assertContainsBlockType(results, ToggleBlock.class);
    assertContainsBlockType(results, ToDoBlock.class);
    assertContainsBlockType(results, NumberedListItemBlock.class);
    assertContainsBlockType(results, CodeBlock.class);
    assertContainsBlockType(results, EquationBlock.class);
    assertContainsBlockType(results, BreadcrumbBlock.class);
    assertContainsBlockType(results, ChildDatabaseBlock.class);
    assertContainsBlockType(results, BookmarkBlock.class);
    assertContainsBlockType(results, SyncedBlock.class);
    assertContainsBlockType(results, TableBlock.class);
    assertContainsBlockType(results, VideoBlock.class);
    assertDoesNotContainBlockType(results, UnknownBlock.class);
  }

  @Test
  void deserialize_retrieveBlocksFixture2_mapsMediaAndSpecialBlockTypes() {
    BlockList blockList = deserializeFixture("json/retrieve-blocsk-rs-2.json");

    assertNotNull(blockList);
    assertNotNull(blockList.getResults());
    assertFalse(blockList.getResults().isEmpty());

    List<Block> results = blockList.getResults();
    assertContainsBlockType(results, ColumnListBlock.class);
    assertContainsBlockType(results, HeadingOneBlock.class);
    assertContainsBlockType(results, HeadingThreeBlock.class);
    assertContainsBlockType(results, ParagraphBlock.class);
    assertContainsBlockType(results, DividerBlock.class);
    assertContainsBlockType(results, ImageBlock.class);
    assertContainsBlockType(results, AudioBlock.class);
    assertContainsBlockType(results, VideoBlock.class);
    assertContainsBlockType(results, FileBlock.class);
    assertContainsBlockType(results, PdfBlock.class);
    assertContainsBlockType(results, EmbedBlock.class);
    assertContainsBlockType(results, BookmarkBlock.class);
    assertContainsBlockType(results, LinkPreviewBlock.class);
    assertContainsBlockType(results, UnsupportedBlock.class);
    assertContainsBlockType(results, CodeBlock.class);
    assertDoesNotContainBlockType(results, UnknownBlock.class);
  }

  private static void assertContainsBlockType(List<Block> blocks, Class<? extends Block> type) {
    assertTrue(
        blocks.stream().anyMatch(type::isInstance), "Expected block type: " + type.getSimpleName());
  }

  private static void assertDoesNotContainBlockType(
      List<Block> blocks, Class<? extends Block> type) {
    assertTrue(
        blocks.stream().noneMatch(type::isInstance),
        "Unexpected block type: " + type.getSimpleName());
  }

  private static BlockList deserializeFixture(String path) {
    try (InputStream in =
        BlockDeserializationTest.class.getClassLoader().getResourceAsStream(path)) {
      if (in == null) {
        fail("Resource not found: " + path);
      }
      String json = new String(in.readAllBytes(), StandardCharsets.UTF_8);
      return JSON.toObject(json, BlockList.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
