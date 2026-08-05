package integration.blocks;

import static io.kristaxlab.notion.fluent.NotionBlocks.blocksBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import integration.BaseIntegrationTest;
import io.kristaxlab.notion.model.block.Block;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.block.BlockType;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IT26_Blocks_SeveralBlocksAtOnce extends BaseIntegrationTest {

  @Test
  @DisplayName("IT-26: Blocks - Create several blocks at once")
  public void testCreateSeveralBlocksAtOnce() {
    List<Block> blocks =
        blocksBuilder()
            .paragraph("paragraph block")
            .bullet("bulleted list block")
            .toggle(
                t -> t.text("toggle block").children(c -> c.paragraph("nested paragraph block")))
            .build();

    BlockList created = getNotionClient().blocks().appendChildren(getTestPageId(), blocks);

    assertEquals(3, created.getResults().size());
    created.getResults().forEach(block -> assertNotNull(block.getId()));

    Block paragraph = created.getResults().get(0);
    assertEquals(BlockType.PARAGRAPH.getValue(), paragraph.getType());

    Block bullet = created.getResults().get(1);
    assertEquals(BlockType.BULLETED_LIST_ITEM.getValue(), bullet.getType());

    Block toggle = created.getResults().get(2);
    assertEquals(BlockType.TOGGLE.getValue(), toggle.getType());
    assertTrue(toggle.getHasChildren(), "Toggle was created with a nested paragraph");

    // all three blocks are persisted on the page keeping the requested order
    List<String> createdIds = List.of(paragraph.getId(), bullet.getId(), toggle.getId());
    BlockList onPage = getNotionClient().blocks().retrieveChildren(getTestPageId());
    assertEquals(
        createdIds,
        onPage.getResults().stream().map(Block::getId).filter(createdIds::contains).toList());
  }
}
