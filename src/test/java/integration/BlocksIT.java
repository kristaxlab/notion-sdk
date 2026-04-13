package integration;

import static io.kristaxlab.notion.model.helper.NotionBlocks.*;
import static io.kristaxlab.notion.model.helper.NotionText.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import integration.helper.IntegrationTestAssisstant;
import io.kristaxlab.notion.http.error.ValidationException;
import io.kristaxlab.notion.model.block.*;
import io.kristaxlab.notion.model.common.Color;
import io.kristaxlab.notion.model.common.Icon;
import io.kristaxlab.notion.model.common.Parent;
import io.kristaxlab.notion.model.common.Position;
import io.kristaxlab.notion.model.common.richtext.RichText;
import io.kristaxlab.notion.model.helper.NotionBlocks;
import java.util.List;
import org.junit.jupiter.api.*;

public class BlocksIT extends BaseIntegrationTest {

  private static String blockTestsPageId;
  private String currTestPageId;

  @BeforeAll
  public static void setup() {
    blockTestsPageId = IntegrationTestAssisstant.createPageForTests("Blocks");
  }

  @BeforeEach
  public void beforeEachTest(TestInfo info) {
    super.beforeEach(info);
    currTestPageId =
        IntegrationTestAssisstant.createPageForTests(
            info.getDisplayName(), Parent.pageParent(blockTestsPageId));
  }

  @Test
  @DisplayName("[IT-22]: Blocks - Creation / update / retrieval for a paragraph block")
  public void testBlockCreationUpdateRetrieval() {
    // Step 1: Create a text block
    BlockList createResult =
        getNotion().blocks().appendChildren(currTestPageId, paragraph("testing text block"));

    assertEquals(1, createResult.getResults().size());

    String blockId = createResult.getResults().get(0).getId();

    // Step 2: Update style to bold, blue
    ParagraphBlock updated1 =
        paragraph(
            p -> p.text(t -> t.plainText("updated text block").bold()).blockColor(Color.BLUE));
    Block updatedBlockRs1 = getNotion().blocks().update(blockId, updated1);

    assertNotNull(updatedBlockRs1);
    assertEquals("paragraph", updatedBlockRs1.getType());
    assertNotNull(updatedBlockRs1.asParagraph().getParagraph());
    assertEquals(1, updatedBlockRs1.asParagraph().getParagraph().getRichText().size());
    assertEquals(Color.BLUE.getValue(), updatedBlockRs1.asParagraph().getParagraph().getColor());
    RichText updatedRichText = updatedBlockRs1.asParagraph().getParagraph().getRichText().get(0);
    assertTrue(updatedRichText.getAnnotations().getBold());

    // Step 3: Update block separating text into two parts: "Text - " (blue) and "updated" (yellow)
    ParagraphBlock updated2 = paragraph(plainText("Text - ").blue(), plainText("updated").yellow());
    Block updatedBlockRs2 = getNotion().blocks().update(blockId, updated2);

    assertNotNull(updatedBlockRs2);
    assertEquals("paragraph", updatedBlockRs2.getType());
    assertEquals(2, updatedBlockRs2.asParagraph().getParagraph().getRichText().size());

    // Step 4: Retrieve block
    Block retrievedBlock = getNotion().blocks().retrieve(blockId);

    assertNotNull(retrievedBlock);
    assertEquals(blockId, retrievedBlock.getId());
  }

  @Test
  @DisplayName("[IT-24]: Blocks - Delete and restore a block")
  public void testBlockDeleteAndRestore() {
    // Step 1: Create a block of type "list" with text "list block"
    BlockList createRs =
        getNotion().blocks().appendChildren(currTestPageId, bullet("bulleted list item"));

    assertNotNull(createRs);
    assertNotNull(createRs.getResults());
    assertEquals(1, createRs.getResults().size());
    Block createdBlock = createRs.getResults().get(0);

    String blockId = createdBlock.getId();

    // Step 2: Delete the block
    Block deletedBlock = getNotion().blocks().delete(blockId);
    assertNotNull(deletedBlock);
    assertEquals(blockId, deletedBlock.getId());

    // Step 3: Retrieve and check that the status is deleted (archived)
    Block retrievedBlock = getNotion().blocks().retrieve(blockId);
    assertNotNull(retrievedBlock);
    assertEquals(blockId, retrievedBlock.getId());
    assertTrue(retrievedBlock.getInTrash(), "Block should be archived after deletion");

    Block restoredBlock = getNotion().blocks().restore(blockId);
    assertNotNull(retrievedBlock);
    assertEquals(blockId, retrievedBlock.getId());
    assertFalse(restoredBlock.getInTrash(), "Block should not be archived after restoration");

    retrievedBlock = getNotion().blocks().retrieve(blockId);
    assertNotNull(retrievedBlock);
    assertEquals(blockId, retrievedBlock.getId());
    assertFalse(retrievedBlock.getInTrash(), "Block should be archived after deletion");
  }

  @Test
  @DisplayName("[IT-26]: Blocks - Create several blocks at once")
  public void testCreateSeveralBlocksAtOnce() {
    List<Block> blocks =
        blocksBuilder()
            .paragraph("paragraph block")
            .bullet("bulleted list block")
            .toggle(
                t -> t.text("toggle block").children(c -> c.paragraph("nested paragraph block")))
            .build();

    BlockList createResult = getNotion().blocks().appendChildren(currTestPageId, blocks);

    assertNotNull(createResult);
    assertNotNull(createResult.getResults());
    assertEquals(3, createResult.getResults().size());

    // Verify paragraph block
    Block firstBlock = createResult.getResults().get(0);
    assertNotNull(firstBlock.getId());
    assertEquals("paragraph", firstBlock.getType());

    // Verify bulleted list item block
    Block secondBlock = createResult.getResults().get(1);
    assertNotNull(secondBlock.getId());
    assertEquals("bulleted_list_item", secondBlock.getType());

    // Verify toggle block
    Block thirdBlock = createResult.getResults().get(2);
    assertNotNull(thirdBlock.getId());
    assertEquals("toggle", thirdBlock.getType());
    assertTrue(thirdBlock.getHasChildren());
  }

  @Test
  @DisplayName("[IT-29]: Blocks - Create empty Paragraph block with nested bullet list")
  public void testBlockWithChildren() {
    ParagraphBlock createBlockRq =
        ParagraphBlock.builder()
            .children(b -> b.bullet("item 1").bullet("item 2").bullet("item 3"))
            .build();

    BlockList response = getNotion().blocks().appendChildren(currTestPageId, createBlockRq);
    assertNotNull(response);
    assertNotNull(response.getResults());
    assertEquals(1, response.getResults().size());
    assertTrue(response.getResults().get(0).getHasChildren());

    String blockId = response.getResults().get(0).getId();
    BlockList retrieveChildrenRs = getNotion().blocks().retrieveChildren(blockId);

    assertNotNull(retrieveChildrenRs);
    assertNotNull(retrieveChildrenRs.getResults());
    assertEquals(3, retrieveChildrenRs.getResults().size());
    retrieveChildrenRs.getResults().forEach(b -> assertEquals(blockId, b.getParent().getBlockId()));
  }

  @Test
  @DisplayName("[IT-30]: Blocks - Change block type (should be validation error)")
  public void testExceptionOnTypeChange() {
    ParagraphBlock createBlockRq = paragraph("Text block");
    BlockList createResult = getNotion().blocks().appendChildren(currTestPageId, createBlockRq);
    String blockId = createResult.getResults().get(0).getId();

    HeadingThreeBlock headingBlock = heading3("heading text");

    assertThrowsExactly(
        ValidationException.class, () -> getNotion().blocks().update(blockId, headingBlock));
  }

  @Test
  @DisplayName("[IT-31]: Blocks - Insert blocks into a specific position")
  public void testInsertToPosition() {

    AppendBlockChildrenParams initialRq =
        AppendBlockChildrenParams.builder()
            .children(List.of(paragraph("initial block 1"), paragraph("initial block 2")))
            .build();

    BlockList createResult = getNotion().blocks().appendChildren(currTestPageId, initialRq);
    String firstInitialBlockId = createResult.getResults().get(0).getId();
    String secondInitialBlockId = createResult.getResults().get(1).getId();

    AppendBlockChildrenParams insertRq =
        AppendBlockChildrenParams.builder()
            .children(paragraph("inserted block"))
            .position(Position.afterBlock(firstInitialBlockId))
            .build();

    BlockList insertRs = getNotion().blocks().appendChildren(currTestPageId, insertRq);
    BlockList allBlocks = getNotion().blocks().retrieveChildren(currTestPageId);

    String insertedBlockId = insertRs.getResults().get(0).getId();
    assertNotNull(allBlocks);
    assertNotNull(allBlocks.getResults());
    assertEquals(3, allBlocks.getResults().size());
    assertEquals(firstInitialBlockId, allBlocks.getResults().get(0).getId());
    assertEquals(secondInitialBlockId, allBlocks.getResults().get(2).getId());
    assertEquals(insertedBlockId, allBlocks.getResults().get(1).getId());
  }

  @Test
  @DisplayName("[IT-23]: Blocks & File Uploads - Insert an uploaded file as an image")
  public void insertUploadedFileAsImage() {
    String fileUploadId = IntegrationTestAssisstant.getPrerequisites().getImageFileUploadId();
    ImageBlock imageBlock =
        NotionBlocks.image(
            i -> i.fileUpload(fileUploadId).caption("[IT-23]: An image from uploaded file"));

    BlockList result = getNotion().blocks().appendChildren(currTestPageId, imageBlock);

    assertNotNull(result);
    assertEquals(1, result.getResults().size());
    assertEquals("image", result.getResults().get(0).getType());
  }

  @Test
  @DisplayName("[IT-42]: Blocks - Append textual block types")
  public void testAppendTextualBlocks() {
    List<Block> blocks =
        blocksBuilder()
            .paragraph("paragraph")
            .paragraph(plainText("styled paragraph").red().bold())
            .paragraph(
                p ->
                    p.text(
                            plainText("complex"),
                            plainText(" paragraph ").code().bold(),
                            plainText(" with rich text").italic())
                        .blockColor(Color.GRAY_BACKGROUND))
            .bullet("bulleted list item")
            .bullet(td -> td.text("bulleted list item").blockColor(Color.GRAY))
            .toggle("toggle block")
            .toggle(t -> t.text("toggle w/children").children(c -> c.paragraph("nested text")))
            .todo("todo block")
            .todo(t -> t.checked().text("checked todo"))
            .quote("quote block")
            .code("java", "print('hello world')")
            .callout("🍊", "Call out for oranges")
            .callout(
                c ->
                    c.text("Callout with children")
                        .blockColor(Color.ORANGE_BACKGROUND)
                        .icon(Icon.emoji("⚡"))
                        .children(
                            cb -> cb.divider().paragraph("nested text").paragraph("another one")))
            .equation("e=mc^2")
            .build();
    BlockList result = getNotion().blocks().appendChildren(currTestPageId, blocks);
    assertEquals(blocks.size(), result.getResults().size());
  }

  @Test
  @DisplayName("[IT-57]: Blocks - Append heading block types")
  public void testAppendHeadingBlocks() {

    List<Block> headings =
        blocksBuilder()
            .heading1("simple heading")
            .heading2(h -> h.text("coloured heading").blockColor(Color.BLUE))
            .heading3(
                h ->
                    h.text("heading with children automatically makes it toggleable")
                        .blockColor(Color.ORANGE)
                        .children(cb -> cb.paragraph("nested text").breadcrumb()))
            .heading3(h -> h.text("empty toggleable heading").blockColor("orange").toggleable(true))
            .heading4("other blocks")
            .heading4("Other blocks below")
            .build();

    BlockList result = getNotion().blocks().appendChildren(currTestPageId, headings);
    assertEquals(headings.size(), result.getResults().size());
  }

  @Test
  @DisplayName("[IT-58]: Blocks - Append table block types")
  public void testAppendTableBlock() {
    TableBlock table =
        TableBlock.builder()
            .tableWidth(5)
            .hasColumnHeader(true)
            .rows(
                rows ->
                    rows.row()
                        .cell("Mon")
                        .cell("Tue")
                        .cell("Wed")
                        .cell("Thu")
                        .cell("Fri")
                        .row()
                        .cell("gym")
                        .cell("run")
                        .cell("bike")
                        .cell("gym")
                        .cell("run"))
            .build();

    table =
        NotionBlocks.table(
            NotionBlocks.tableRow("Mon", "Tue", "Wed", "Thu", "Fri"),
            NotionBlocks.tableRow("gym", "run", "bike", "gym", "run"));

    BlockList result = getNotion().blocks().appendChildren(currTestPageId, table);
    assertEquals(1, result.getResults().size());
    assertTrue(result.getResults().get(0).getHasChildren());

    BlockList rows = getNotion().blocks().retrieveChildren(result.getResults().get(0).getId());
    assertEquals(2, rows.getResults().size());
    assertEquals("table_row", rows.getResults().get(0).getType());
    assertEquals("table_row", rows.getResults().get(1).getType());
    assertEquals(5, rows.getResults().get(0).asTableRow().getTableRow().getCells().size());
    assertEquals(5, rows.getResults().get(1).asTableRow().getTableRow().getCells().size());
  }

  @Test
  @DisplayName("[IT-59]: Append links and media block types")
  public void testAppendLinksAndMediaBlocks() {
    List<Block> linksAndMedia =
        blocksBuilder()
            .bookmark("https://www.notion.so")
            .bookmark(
                b ->
                    b.url("https://github.com/kristaxlab/notion-sdk")
                        .caption("Notion SDK on GitHub"))
            .image(
                "https://www.notion.com/_next/image?url=%2Ffront-static%2Fagents%2Fglobe.png&w=96&q=75")
            .embed("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
            .linkToPage(currTestPageId)
            .linkToDatabase(IntegrationTestAssisstant.getPrerequisites().getTestDatabaseId())
            .build();

    BlockList result = getNotion().blocks().appendChildren(currTestPageId, linksAndMedia);
    assertEquals(linksAndMedia.size(), result.getResults().size());
  }

  @Test
  @DisplayName("[IT-60]: Append synced blocks")
  public void testAppendSynchedBlock() {
    SyncedBlock original =
        NotionBlocks.synced(
            b ->
                b.paragraph("This is the original synced block content.")
                    .bullet("Original item 1")
                    .bullet("Original item 2"));

    BlockList originalBlockRS = getNotion().blocks().appendChildren(currTestPageId, original);
    assertEquals(1, originalBlockRS.getResults().size());
    assertEquals("synced_block", originalBlockRS.getResults().get(0).getType());
    assertNull(originalBlockRS.getResults().get(0).asSynced().getSyncedBlock().getSyncedFrom());

    String originalBlockId = originalBlockRS.getResults().get(0).getId();
    SyncedBlock synced = NotionBlocks.syncedFrom(originalBlockId);
    BlockList syncedBlockRS = getNotion().blocks().appendChildren(currTestPageId, synced);

    SyncedBlock savedSyncedBlock = syncedBlockRS.getResults().get(0).asSynced();
    assertEquals(1, syncedBlockRS.getResults().size());
    assertEquals(originalBlockId, savedSyncedBlock.getSyncedBlock().getSyncedFrom().getBlockId());

    // unsynking is not supported

    savedSyncedBlock.getSyncedBlock().setSyncedFrom(null);

    assertThrows(
        ValidationException.class,
        () -> getNotion().blocks().update(savedSyncedBlock.getId(), savedSyncedBlock));
  }

  @Test
  @DisplayName("[IT-61]: Append other block types (not included in INT-42, 57, 58, 59)")
  public void testAppendOtherBlock() {

    List<Block> other =
        blocksBuilder()
            .breadcrumb()
            .tableOfContents(Color.YELLOW)
            .columns(
                left -> left.heading1("To Do list").todo("Item 1").todo("Item 2"),
                middle -> middle.heading1("Numbered list").numbered("Item 1").numbered("Item 2"),
                right -> right.heading1("Bulleted list").bullet("Item 1").bullet("Item 2"))
            .build();

    BlockList resultOther = getNotion().blocks().appendChildren(currTestPageId, other);
    assertEquals(other.size(), resultOther.getResults().size());
  }
}
