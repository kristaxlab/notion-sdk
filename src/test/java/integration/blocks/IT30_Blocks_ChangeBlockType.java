package integration.blocks;

import static io.kristaxlab.notion.fluent.NotionBlocks.heading3;
import static io.kristaxlab.notion.fluent.NotionBlocks.paragraph;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import integration.BaseIntegrationTest;
import io.kristaxlab.notion.http.error.ValidationException;
import io.kristaxlab.notion.model.block.Block;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.block.BlockType;
import io.kristaxlab.notion.model.block.HeadingThreeBlock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IT30_Blocks_ChangeBlockType extends BaseIntegrationTest {

  @Test
  @DisplayName("IT-30: Blocks - Change block type (should be validation error)")
  public void testTypeChangeIsRejected() {
    // 1. Create a paragraph block
    BlockList created =
        getNotionClient().blocks().appendChildren(getTestPageId(), paragraph("Text block"));

    String blockId = created.getResults().get(0).getId();

    // 2. Update it to a heading 3 - Notion does not allow changing the type of an existing block
    HeadingThreeBlock heading = heading3("heading text");

    assertThrowsExactly(
        ValidationException.class, () -> getNotionClient().blocks().update(blockId, heading));

    // the rejected update leaves the block untouched
    Block unchanged = getNotionClient().blocks().retrieve(blockId);
    assertEquals(BlockType.PARAGRAPH.getValue(), unchanged.getType());
  }
}
