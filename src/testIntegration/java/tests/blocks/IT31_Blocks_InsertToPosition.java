package tests.blocks;

import static io.kristaxlab.notion.fluent.NotionBlocks.paragraph;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.kristaxlab.notion.model.block.AppendBlockChildrenParams;
import io.kristaxlab.notion.model.block.Block;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.common.Position;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

public class IT31_Blocks_InsertToPosition extends WithEmptyTestPage {

  @Test
  @DisplayName("IT-31: Blocks - Insert blocks into a specific position")
  public void testInsertBlocksToPosition() {
    // 1. Create two text blocks
    BlockList initial =
        getNotionClient()
            .blocks()
            .appendChildren(
                getTestPageId(),
                AppendBlockChildrenParams.builder()
                    .children(List.of(paragraph("initial block 1"), paragraph("initial block 2")))
                    .build());

    assertEquals(2, initial.getResults().size());
    String firstInitialId = initial.getResults().get(0).getId();
    String secondInitialId = initial.getResults().get(1).getId();

    // 2. Insert two more text blocks between the ones created previously
    BlockList inserted =
        getNotionClient()
            .blocks()
            .appendChildren(
                getTestPageId(),
                AppendBlockChildrenParams.builder()
                    .children(List.of(paragraph("inserted block 1"), paragraph("inserted block 2")))
                    .position(Position.afterBlock(firstInitialId))
                    .build());

    // Notion answers with everything that follows the anchor block: the newly inserted blocks
    // and the already existing siblings they pushed down
    assertEquals(3, inserted.getResults().size());
    assertEquals(secondInitialId, inserted.getResults().get(2).getId());

    List<String> insertedIds =
        inserted.getResults().stream()
            .map(Block::getId)
            .filter(id -> !id.equals(secondInitialId))
            .toList();
    assertEquals(2, insertedIds.size());

    List<String> expectedOrder = new ArrayList<>();
    expectedOrder.add(firstInitialId);
    expectedOrder.addAll(insertedIds);
    expectedOrder.add(secondInitialId);

    BlockList onPage = getNotionClient().blocks().retrieveChildren(getTestPageId());
    assertEquals(
        expectedOrder,
        onPage.getResults().stream().map(Block::getId).filter(expectedOrder::contains).toList(),
        "Inserted blocks should sit between the two initial ones");
  }
}
