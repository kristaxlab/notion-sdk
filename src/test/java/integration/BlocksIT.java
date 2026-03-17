package integration;

import static org.junit.jupiter.api.Assertions.*;

import integration.util.IntegrationTestAssisstant;
import io.kristixlab.notion.api.http.exception.ValidationException;
import io.kristixlab.notion.api.model.blocks.*;
import io.kristixlab.notion.api.model.common.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;

public class BlocksIT extends BaseIntegrationTest {

  private static String blockTestsPageId;
  private String currTestPageId;

  @BeforeAll
  public static void setup() {
    blockTestsPageId = IntegrationTestAssisstant.createTestsPage("Blocks");
  }

  @BeforeEach
  public void beforeEachTest(TestInfo info) {
    super.beforeEach(info);
    currTestPageId =
        IntegrationTestAssisstant.createTestsPage(info.getDisplayName(), blockTestsPageId);
  }

  //  @Test
  //  @DisplayName("[IT-31]: Creation of block with builders
  //  public void testMentionBlocks() {
  //
  //    BlockList dateMention =
  //        getNotion()
  //            .blocks()
  //            .appendChildren(
  //                currTestPageId,
  //                ParagraphBlock.builder().fromDateMention("2026-03-11")
  //                        .addAnother().fromText("\n")
  //                        .addAnother().fromBlockMention(currTestPageId)
  //                        .addAnother().fromText("\n")
  //                        .addAnother().fromExpression("x+y=10")
  //                        .addAnother().fromText("\n")
  //                        .addAnother().fromText("pwd").withCode(true)
  //                        .addAnother().fromText(" ")
  //                        .addAnother().fromText("Hello,").withColor(Color.PURPLE).withCode(true)
  //                        .addAnother().fromText(" ")
  //
  // .addAnother().fromText("Notion").withColor(Color.ORANGE).withBold(true).withUnderline(true)
  //                        .build());
  //
  //    assertNotNull(dateMention);
  //    assertEquals(1, dateMention.getResults().size());
  //    assertEquals("paragraph", dateMention.getResults().get(0).getType());
  //    assertEquals(11,
  // dateMention.getResults().get(0).asParagraph().getParagraph().getRichText().size());
  //
  //  }

  @Test
  @DisplayName("[IT-22]: Blocks - Creation / update / retrieval for a paragraph block")
  public void testBlockCreationUpdateRetrieval() {
    // Step 1: Create a text block "IT-22 - testing text block" on a testing page
    ParagraphBlock createBlockRq = ParagraphBlock.of("testing text block");

    BlockList createResult = getNotion().blocks().appendChildren(currTestPageId, createBlockRq);
    assertNotNull(createResult);
    assertNotNull(createResult.getResults());
    assertEquals(1, createResult.getResults().size());
    String blockId = createResult.getResults().get(0).getId();

    // Step 2: Update style to bold, blue
    ParagraphBlock updateBlockRq1 = ParagraphBlock.of("updated text block", Color.BLUE);
    updateBlockRq1.getParagraph().getRichText().get(0).getAnnotations().setBold(true);

    Block updatedBlockRs1 = getNotion().blocks().update(blockId, updateBlockRq1);
    assertNotNull(updatedBlockRs1);
    assertEquals("paragraph", updatedBlockRs1.getType());
    assertNotNull(updatedBlockRs1.asParagraph().getParagraph());
    assertEquals(1, updatedBlockRs1.asParagraph().getParagraph().getRichText().size());
    assertEquals(Color.BLUE.getValue(), updatedBlockRs1.asParagraph().getParagraph().getColor());
    RichText updatedRichText = updatedBlockRs1.asParagraph().getParagraph().getRichText().get(0);
    assertTrue(updatedRichText.getAnnotations().getBold());

    // Step 3: Update block separating text into two parts: "Text - " (blue) and "updated" (yellow)
    ParagraphBlock updateBlockRq2 = new ParagraphBlock();

    updateBlockRq2
        .getParagraph()
        .setRichText(
            RichText.builder()
                .fromText("Text - ")
                .withColor(Color.BLUE)
                .addAnother()
                .fromText("updated")
                .withColor(Color.YELLOW)
                .buildAsList());

    Block updatedBlockRs2 = getNotion().blocks().update(blockId, updateBlockRq2);
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
    BulletedListItemBlock createRq = BulletedListItemBlock.of("bulleted list item");

    BlockList createRs = getNotion().blocks().appendChildren(currTestPageId, createRq);

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
    List<Block> blocks = new ArrayList<>();
    blocks.add(ParagraphBlock.of("paragraph block"));
    blocks.add(BulletedListItemBlock.of("bulleted list block"));
    ToggleBlock toggleBlock = ToggleBlock.of("toggle block");
    blocks.add(toggleBlock);
    toggleBlock.getToggle().setChildren(new ArrayList<>());
    toggleBlock.getToggle().getChildren().add(ParagraphBlock.of("nested paragraph block"));
    AppendBlockChildrenParams request = AppendBlockChildrenParams.of(blocks);

    BlockList createResult = getNotion().blocks().appendChildren(currTestPageId, request);

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
    assertTrue(thirdBlock.hasChildren());
  }

  @Test
  @DisplayName("[IT-29]: Blocks - Create empty Paragraph block with nested bullet list")
  public void testBlockWithChildren() {
    ParagraphBlock createBlockRq = new ParagraphBlock();
    List<Block> children = new ArrayList<>();
    children.add(BulletedListItemBlock.of("item 1"));
    children.add(BulletedListItemBlock.of("item 2"));
    children.add(BulletedListItemBlock.of("item 3"));
    createBlockRq.getParagraph().setChildren(children);

    BlockList response = getNotion().blocks().appendChildren(currTestPageId, createBlockRq);
    assertNotNull(response);
    assertNotNull(response.getResults());
    assertEquals(1, response.getResults().size());
    assertTrue(response.getResults().get(0).hasChildren());

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
    ParagraphBlock createBlockRq = ParagraphBlock.of("Text block");
    BlockList createResult = getNotion().blocks().appendChildren(currTestPageId, createBlockRq);
    String blockId = createResult.getResults().get(0).getId();

    HeadingThreeBlock headingBlock = new HeadingThreeBlock();
    HeadingThreeBlock.Heading heading = new HeadingThreeBlock.Heading();
    List<RichText> richTexts = RichText.builder().fromText("heading text").buildAsList();
    heading.setRichText(richTexts);

    headingBlock.setHeading3(heading);

    assertThrowsExactly(
        ValidationException.class, () -> getNotion().blocks().update(blockId, headingBlock));
  }

  @Test
  @DisplayName("[IT-31]: Blocks - Insert blocks into a specific position")
  public void testInsertToPosition() {

    AppendBlockChildrenParams initialRq =
        AppendBlockChildrenParams.of(
            List.of(ParagraphBlock.of("initial block 1"), ParagraphBlock.of("initial block 2")));

    BlockList createResult = getNotion().blocks().appendChildren(currTestPageId, initialRq);
    String firstInitialBlockId = createResult.getResults().get(0).getId();
    String secondInitialBlockId = createResult.getResults().get(1).getId();

    AppendBlockChildrenParams insertRq =
        AppendBlockChildrenParams.of(List.of(ParagraphBlock.of("inserted block")));
    insertRq.setPosition(new Position(PositionType.AFTER_BLOCK.getValue(), firstInitialBlockId));

    BlockList insertRs = getNotion().blocks().appendChildren(currTestPageId, insertRq);
    BlockList allBlocks = getNotion().blocks().retrieveChildren(currTestPageId);

    String insertedBlockId = insertRs.getResults().get(0).getId();
    assertNotNull(allBlocks);
    assertNotNull(allBlocks.getResults());
    assertEquals(4, allBlocks.getResults().size());
    assertEquals(firstInitialBlockId, allBlocks.getResults().get(0).getId());
    assertEquals(secondInitialBlockId, allBlocks.getResults().get(2).getId());
    assertEquals(insertedBlockId, allBlocks.getResults().get(1).getId());
  }

  @Test
  @DisplayName("[IT-]: Blocks - Append different block types")
  public void testAppendDifferentBlocks() {
    AppendBlockChildrenParams initialRq = new AppendBlockChildrenParams();
    List<Block> blocks = new ArrayList<>();
    blocks.add(ParagraphBlock.of("paragraph block"));
    blocks.add(BulletedListItemBlock.of("bulleted list item block"));
    ToggleBlock toggleBlock = ToggleBlock.of("toggle block");
  }
}
