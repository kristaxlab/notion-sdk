package integration.blocks;

import static io.kristaxlab.notion.fluent.NotionBlocks.blocksBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

public class IT57_Blocks_HeadingBlocks extends WithEmptyTestPage {

  @Test
  @DisplayName("IT-57: Blocks - Append heading block types")
  public void testAppendHeadingBlocks() {
    List<Block> headings =
        blocksBuilder()
            .heading1("simple heading")
            .heading2(h -> h.text("coloured heading").blockColor(Color.BLUE))
            .heading3(
                h ->
                    h.text("heading with children")
                        .blockColor(Color.ORANGE)
                        .children(cb -> cb.paragraph("nested text").breadcrumb()))
            .heading3(h -> h.text("empty toggleable heading").toggleable(true))
            .heading4("simple heading four")
            .build();

    BlockList appended = getNotionClient().blocks().appendChildren(getTestPageId(), headings);

    assertEquals(headings.size(), appended.getResults().size());
    assertEquals(
        List.of(
            BlockType.HEADING_1.getValue(),
            BlockType.HEADING_2.getValue(),
            BlockType.HEADING_3.getValue(),
            BlockType.HEADING_3.getValue(),
            BlockType.HEADING_4.getValue()),
        appended.getResults().stream().map(Block::getType).toList());

    List<Block> saved = appended.getResults();

    Block headingOne = saved.get(0);
    assertEquals("simple heading", NotionBlocksViewer.of(headingOne).plainText());
    assertFalse(headingOne.asHeadingOne().getHeading1().getIsToggleable());

    Block headingTwo = saved.get(1);
    assertEquals(Color.BLUE.getValue(), headingTwo.asHeadingTwo().getHeading2().getColor());

    // Notion turns a heading with children into a toggleable one on its own
    Block headingWithChildren = saved.get(2);
    assertEquals(
        Color.ORANGE.getValue(), headingWithChildren.asHeadingThree().getHeading3().getColor());
    assertTrue(headingWithChildren.getHasChildren());
    assertTrue(headingWithChildren.asHeadingThree().getHeading3().getIsToggleable());
    assertEquals(
        2,
        getNotionClient()
            .blocks()
            .retrieveChildren(headingWithChildren.getId())
            .getResults()
            .size());

    // an explicitly toggleable heading stays toggleable even without children
    Block emptyToggleable = saved.get(3);
    assertFalse(emptyToggleable.getHasChildren());
    assertTrue(emptyToggleable.asHeadingThree().getHeading3().getIsToggleable());

    Block headingFour = saved.get(4);
    assertEquals("simple heading four", NotionBlocksViewer.of(headingFour).plainText());
  }
}
