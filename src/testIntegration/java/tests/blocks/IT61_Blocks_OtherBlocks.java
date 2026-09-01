package tests.blocks;

import static io.kristaxlab.notion.fluent.NotionBlocks.blocksBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristaxlab.notion.fluent.NotionBlocksViewer;
import io.kristaxlab.notion.model.block.Block;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.block.BlockType;
import io.kristaxlab.notion.model.common.Color;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

public class IT61_Blocks_OtherBlocks extends WithEmptyTestPage {

  @Test
  @DisplayName("IT-61: Blocks - Append other block types (not included in IT-42, 57, 58, 59, 60)")
  public void testAppendOtherBlocks() {
    List<Block> other =
        blocksBuilder()
            .breadcrumb()
            .tableOfContents(Color.YELLOW)
            .columns(
                left -> left.heading1("To Do list").todo("Item 1").todo("Item 2"),
                middle -> middle.heading1("Numbered list").numbered("Item 1").numbered("Item 2"),
                right -> right.heading1("Bulleted list").bullet("Item 1").bullet("Item 2"))
            .build();

    BlockList appended = getNotionClient().blocks().appendChildren(getTestPageId(), other);

    assertEquals(other.size(), appended.getResults().size());
    assertEquals(
        List.of(
            BlockType.BREADCRUMB.getValue(),
            BlockType.TABLE_OF_CONTENTS.getValue(),
            BlockType.COLUMN_LIST.getValue()),
        appended.getResults().stream().map(Block::getType).toList());

    Block tableOfContents = appended.getResults().get(1);
    assertEquals(
        Color.YELLOW.getValue(),
        tableOfContents.asTableOfContents().getTableOfContents().getColor());

    // a column list keeps its columns as children, each column holds its own content
    Block columnList = appended.getResults().get(2);
    assertTrue(columnList.getHasChildren());

    BlockList columns = getNotionClient().blocks().retrieveChildren(columnList.getId());
    assertEquals(3, columns.getResults().size());
    columns
        .getResults()
        .forEach(
            column -> {
              assertEquals(BlockType.COLUMN.getValue(), column.getType());
              assertTrue(column.getHasChildren());
            });

    assertEquals(
        List.of("To Do list", "Item 1", "Item 2"),
        NotionBlocksViewer.of(
                getNotionClient().blocks().retrieveChildren(columns.getResults().get(0).getId()))
            .plainTextList());
  }
}
