package integration.blocks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import integration.BaseIntegrationTest;
import io.kristaxlab.notion.fluent.NotionBlocksViewer;
import io.kristaxlab.notion.model.block.Block;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.block.BlockType;
import io.kristaxlab.notion.model.block.ParagraphBlock;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IT29_Blocks_NestedBulletList extends BaseIntegrationTest {

  @Test
  @DisplayName("IT-29: Blocks - Create empty Paragraph block with nested bullet list")
  public void testEmptyParagraphWithNestedBulletList() {
    // 1. Create an empty paragraph block carrying a nested bulleted list
    ParagraphBlock paragraphWithBullets =
        ParagraphBlock.builder()
            .children(b -> b.bullet("item 1").bullet("item 2").bullet("item 3"))
            .build();

    BlockList created =
        getNotionClient().blocks().appendChildren(getTestPageId(), paragraphWithBullets);

    assertEquals(1, created.getResults().size());

    Block paragraph = created.getResults().get(0);
    assertEquals(BlockType.PARAGRAPH.getValue(), paragraph.getType());
    assertTrue(paragraph.getHasChildren(), "Paragraph should report the nested bullets");
    assertEquals("", NotionBlocksViewer.of(paragraph).plainText(), "Paragraph itself has no text");

    // 2. Retrieve the paragraph children - they are the bulleted list items
    String paragraphId = paragraph.getId();
    BlockList children = getNotionClient().blocks().retrieveChildren(paragraphId);

    assertEquals(3, children.getResults().size());
    children
        .getResults()
        .forEach(
            bullet -> {
              assertEquals(BlockType.BULLETED_LIST_ITEM.getValue(), bullet.getType());
              assertEquals(paragraphId, bullet.getParent().getBlockId());
            });
    assertEquals(
        List.of("item 1", "item 2", "item 3"),
        NotionBlocksViewer.of(children).plainTextList(),
        "Nested bullets should keep the requested order and text");
  }
}
