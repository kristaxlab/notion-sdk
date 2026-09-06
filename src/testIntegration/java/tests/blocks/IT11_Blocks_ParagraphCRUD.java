package tests.blocks;

import static io.kristaxlab.notion.fluent.NotionBlocks.paragraph;
import static io.kristaxlab.notion.fluent.NotionText.plainText;
import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.fluent.NotionBlocksViewer;
import io.kristaxlab.notion.model.block.Block;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.block.BlockType;
import io.kristaxlab.notion.model.common.Color;
import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

public class IT11_Blocks_ParagraphCRUD extends WithEmptyTestPage {

  @Test
  @DisplayName("IT-11: Blocks - Creation / update / retrieval for a paragraph block")
  public void testParagraphCreateUpdateRetrieve() {
    // 1. Create a paragraph block
    BlockList created =
        getNotionClient().blocks().appendChildren(getTestPageId(), paragraph("testing text block"));

    assertEquals(1, created.getResults().size());

    Block createdBlock = created.getResults().get(0);
    assertEquals(BlockType.PARAGRAPH.getValue(), createdBlock.getType());
    assertEquals("testing text block", NotionBlocksViewer.of(createdBlock).plainText());

    String blockId = createdBlock.getId();

    // 2. Update the block making its text bold and the block itself blue
    Block boldAndBlue =
        getNotionClient()
            .blocks()
            .update(
                blockId,
                paragraph(
                    p ->
                        p.text(t -> t.plainText("updated text block").bold())
                            .blockColor(Color.BLUE)));

    assertEquals(BlockType.PARAGRAPH.getValue(), boldAndBlue.getType());
    assertEquals(Color.BLUE.getValue(), boldAndBlue.asParagraph().getParagraph().getColor());

    List<RichText> boldText = boldAndBlue.asParagraph().getParagraph().getRichText();
    assertEquals(1, boldText.size());
    assertEquals("updated text block", boldText.get(0).getPlainText());
    assertTrue(boldText.get(0).getAnnotations().getBold(), "Text should be bold after the update");

    // 3. Update the block so that it consists of two differently coloured text parts
    Block twoParts =
        getNotionClient()
            .blocks()
            .update(blockId, paragraph(plainText("Text - ").blue(), plainText("updated").yellow()));

    List<RichText> parts = twoParts.asParagraph().getParagraph().getRichText();
    assertEquals(2, parts.size());
    assertEquals("Text - ", parts.get(0).getPlainText());
    assertEquals(Color.BLUE.getValue(), parts.get(0).getAnnotations().getColor());
    assertEquals("updated", parts.get(1).getPlainText());
    assertEquals(Color.YELLOW.getValue(), parts.get(1).getAnnotations().getColor());

    // 4. Retrieve the block and check it exposes the latest state
    Block retrieved = getNotionClient().blocks().retrieve(blockId);

    assertEquals(blockId, retrieved.getId());
    assertEquals(BlockType.PARAGRAPH.getValue(), retrieved.getType());
    assertEquals("Text - updated", NotionBlocksViewer.of(retrieved).plainText());

    // 5. Delete the block
    Block deleted = getNotionClient().blocks().moveToTrash(blockId);
    assertTrue(deleted.getInTrash(), "Block should be archived after deletion");

    // 6. Restore the block
    Block restored = getNotionClient().blocks().restore(blockId);
    assertFalse(restored.getInTrash(), "Block should be restored after deletion");
  }
}
