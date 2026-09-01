package integration.blocks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristaxlab.notion.model.block.Block;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.block.BlockType;
import io.kristaxlab.notion.model.block.TableBlock;
import io.kristaxlab.notion.model.block.TableRowBlock;
import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

public class IT58_Blocks_TableBlocks extends WithEmptyTestPage {

  private static final List<String> HEADER_ROW = List.of("Mon", "Tue", "Wed", "Thu", "Fri");
  private static final List<String> DATA_ROW = List.of("gym", "run", "bike", "gym", "run");

  @Test
  @DisplayName("IT-58: Blocks - Append table block types")
  public void testAppendTableBlock() {
    TableBlock table =
        TableBlock.builder()
            .tableWidth(HEADER_ROW.size())
            .hasColumnHeader(true)
            .rows(
                rows ->
                    rows.row(HEADER_ROW.toArray(String[]::new))
                        .row(DATA_ROW.toArray(String[]::new)))
            .build();

    BlockList appended = getNotionClient().blocks().appendChildren(getTestPageId(), table);

    assertEquals(1, appended.getResults().size());

    Block savedTable = appended.getResults().get(0);
    assertEquals(BlockType.TABLE.getValue(), savedTable.getType());
    assertTrue(savedTable.getHasChildren(), "Table rows are children of the table block");
    assertEquals(HEADER_ROW.size(), savedTable.asTable().getTable().getTableWidth());
    assertTrue(savedTable.asTable().getTable().isHasColumnHeader());

    // rows are not part of the append response, they have to be retrieved separately
    BlockList rows = getNotionClient().blocks().retrieveChildren(savedTable.getId());

    assertEquals(2, rows.getResults().size());
    rows.getResults().forEach(row -> assertEquals(BlockType.TABLE_ROW.getValue(), row.getType()));
    assertEquals(HEADER_ROW, cellTexts(rows.getResults().get(0).asTableRow()));
    assertEquals(DATA_ROW, cellTexts(rows.getResults().get(1).asTableRow()));
  }

  private static List<String> cellTexts(TableRowBlock row) {
    return row.getTableRow().getCells().stream()
        .map(cell -> cell.stream().map(RichText::getPlainText).reduce("", String::concat))
        .toList();
  }
}
