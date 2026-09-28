package tests.blocks;

import static io.kristaxlab.notion.fluent.NotionBlocks.tab;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristaxlab.notion.model.block.Block;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.block.BlockType;
import io.kristaxlab.notion.model.block.ParagraphBlock;
import io.kristaxlab.notion.model.block.TabBlock;
import io.kristaxlab.notion.model.common.Icon;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

public class IT23_Blocks_TabBlocks extends WithEmptyTestPage {

  @Test
  @DisplayName("IT-23: Blocks - Append tab with labeled paragraph children and retrieve strip")
  public void testAppendTabWithParagraphChildren() {
    TabBlock tab =
        tab(
            ParagraphBlock.builder()
                .text("Overview")
                .icon(Icon.emoji("📋"))
                .children(c -> c.paragraph("Tab 1 content"))
                .build(),
            ParagraphBlock.builder()
                .text("Details")
                .icon(Icon.emoji("🔍"))
                .children(c -> c.paragraph("Tab 2 content"))
                .build());

    BlockList appended = getNotionClient().blocks().appendChildren(getTestPageId(), tab);

    assertEquals(1, appended.getResults().size());
    Block created = appended.getResults().get(0);
    assertEquals(BlockType.TAB.getValue(), created.getType());
    assertInstanceOf(TabBlock.class, created);
    assertTrue(created.getHasChildren());

    BlockList strip = getNotionClient().blocks().retrieveChildren(created.getId());
    assertEquals(2, strip.getResults().size());
    strip
        .getResults()
        .forEach(child -> assertEquals(BlockType.PARAGRAPH.getValue(), child.getType()));

    ParagraphBlock first = strip.getResults().get(0).asParagraph();
    assertEquals("Overview", first.getParagraph().getRichText().get(0).getPlainText());
    assertEquals("📋", first.getParagraph().getIcon().getEmoji());
    assertTrue(first.getHasChildren());

    ParagraphBlock second = strip.getResults().get(1).asParagraph();
    assertEquals("Details", second.getParagraph().getRichText().get(0).getPlainText());

    BlockList panel = getNotionClient().blocks().retrieveChildren(first.getId());
    assertEquals(
        List.of("Tab 1 content"),
        panel.getResults().stream()
            .map(b -> b.asParagraph().getParagraph().getRichText().get(0).getPlainText())
            .toList());
  }
}
