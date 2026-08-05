package integration.blocks;

import static io.kristaxlab.notion.fluent.NotionBlocks.blocksBuilder;
import static io.kristaxlab.notion.fluent.NotionText.plainText;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import integration.BaseIntegrationTest;
import io.kristaxlab.notion.fluent.NotionBlocksViewer;
import io.kristaxlab.notion.model.block.Block;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.block.BlockType;
import io.kristaxlab.notion.model.common.Color;
import io.kristaxlab.notion.model.common.Icon;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IT42_Blocks_TextualBlocks extends BaseIntegrationTest {

  @Test
  @DisplayName("IT-42: Blocks - Append textual block types")
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
            .bullet(b -> b.text("coloured bulleted list item").blockColor(Color.GRAY))
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

    BlockList appended = getNotionClient().blocks().appendChildren(getTestPageId(), blocks);

    assertEquals(blocks.size(), appended.getResults().size());
    assertEquals(
        List.of(
            BlockType.PARAGRAPH.getValue(),
            BlockType.PARAGRAPH.getValue(),
            BlockType.PARAGRAPH.getValue(),
            BlockType.BULLETED_LIST_ITEM.getValue(),
            BlockType.BULLETED_LIST_ITEM.getValue(),
            BlockType.TOGGLE.getValue(),
            BlockType.TOGGLE.getValue(),
            BlockType.TO_DO.getValue(),
            BlockType.TO_DO.getValue(),
            BlockType.QUOTE.getValue(),
            BlockType.CODE.getValue(),
            BlockType.CALLOUT.getValue(),
            BlockType.CALLOUT.getValue(),
            BlockType.EQUATION.getValue()),
        appended.getResults().stream().map(Block::getType).toList());

    List<Block> saved = appended.getResults();

    // block level colour and per run annotations survive the round trip
    assertEquals(
        Color.GRAY_BACKGROUND.getValue(), saved.get(2).asParagraph().getParagraph().getColor());
    assertEquals(
        "complex paragraph  with rich text", NotionBlocksViewer.of(saved.get(2)).plainText());
    assertEquals(
        Color.GRAY.getValue(), saved.get(4).asBulletedListItem().getBulletedListItem().getColor());

    // children requested inline are attached to their parent block
    assertTrue(saved.get(6).getHasChildren(), "Toggle was appended with a nested paragraph");
    assertEquals(
        1, getNotionClient().blocks().retrieveChildren(saved.get(6).getId()).getResults().size());

    assertTrue(saved.get(8).asToDo().getToDo().getChecked(), "Second todo was appended as checked");

    assertEquals("java", saved.get(10).asCode().getCode().getLanguage());
    assertEquals("print('hello world')", NotionBlocksViewer.of(saved.get(10)).plainText());

    assertEquals("🍊", saved.get(11).asCallout().getCallout().getIcon().getEmoji());

    assertEquals("⚡", saved.get(12).asCallout().getCallout().getIcon().getEmoji());
    assertEquals(
        Color.ORANGE_BACKGROUND.getValue(), saved.get(12).asCallout().getCallout().getColor());
    assertEquals(
        3, getNotionClient().blocks().retrieveChildren(saved.get(12).getId()).getResults().size());

    assertEquals("e=mc^2", saved.get(13).asEquation().getEquation().getExpression());
  }
}
