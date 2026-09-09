package tests.blocks;

import static io.kristaxlab.notion.fluent.NotionBlocks.blocksBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.kristaxlab.notion.model.block.Block;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.block.BlockType;
import io.kristaxlab.notion.model.common.richtext.RichText;
import io.kristaxlab.notion.model.database.CreateDatabaseParams;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

public class IT20_Blocks_LinksAndMedia extends WithEmptyTestPage {

  private static final String BOOKMARK_URL = "https://www.notion.so";
  private static final String CAPTIONED_BOOKMARK_URL = "https://github.com/kristaxlab/notion-sdk";
  private static final String BOOKMARK_CAPTION = "Notion SDK on GitHub";
  private static final String IMAGE_URL =
      "https://www.notion.com/_next/image?url=%2Ffront-static%2Fagents%2Fglobe.png&w=96&q=75";
  private static final String EMBED_URL = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";

  private String databaseId;

  /** A link-to-database block needs an existing database to point at. */
  @BeforeEach
  public void setup() {
    databaseId =
        getSetupClient()
            .databases()
            .create(
                CreateDatabaseParams.builder()
                    .inPage(getTestPageId())
                    .title("Database to link to")
                    .properties(p -> p.title("Name"))
                    .build())
            .getId();
  }

  @Test
  @DisplayName("IT-20: Blocks - Append links and media block types")
  public void testAppendLinksAndMediaBlocks() {
    List<Block> linksAndMedia =
        blocksBuilder()
            .bookmark(BOOKMARK_URL)
            .bookmark(b -> b.url(CAPTIONED_BOOKMARK_URL).caption(BOOKMARK_CAPTION))
            .image(IMAGE_URL)
            .embed(EMBED_URL)
            .linkToPage(getTestPageId())
            .linkToDatabase(databaseId)
            .build();

    BlockList appended = getNotionClient().blocks().appendChildren(getTestPageId(), linksAndMedia);

    assertEquals(linksAndMedia.size(), appended.getResults().size());
    assertEquals(
        List.of(
            BlockType.BOOKMARK.getValue(),
            BlockType.BOOKMARK.getValue(),
            BlockType.IMAGE.getValue(),
            BlockType.EMBED.getValue(),
            BlockType.LINK_TO_PAGE.getValue(),
            BlockType.LINK_TO_PAGE.getValue()),
        appended.getResults().stream().map(Block::getType).toList());

    List<Block> saved = appended.getResults();

    assertEquals(BOOKMARK_URL, saved.get(0).asBookmark().getBookmark().getUrl());

    assertEquals(CAPTIONED_BOOKMARK_URL, saved.get(1).asBookmark().getBookmark().getUrl());
    assertEquals(BOOKMARK_CAPTION, plainText(saved.get(1).asBookmark().getBookmark().getCaption()));

    // an image referenced by URL stays external, Notion does not host a copy of it
    assertEquals("external", saved.get(2).asImage().getImage().getType());
    assertEquals(IMAGE_URL, saved.get(2).asImage().getImage().getExternal().getUrl());

    assertEquals(EMBED_URL, saved.get(3).asEmbed().getEmbed().getUrl());

    // both page and database links are exposed as link_to_page, discriminated by the inner type
    assertEquals("page_id", saved.get(4).asLinkToPage().getLinkToPage().getType());
    assertEquals(
        withoutDashes(getTestPageId()),
        withoutDashes(saved.get(4).asLinkToPage().getLinkToPage().getPageId()));

    assertEquals("database_id", saved.get(5).asLinkToPage().getLinkToPage().getType());
    assertEquals(
        withoutDashes(databaseId),
        withoutDashes(saved.get(5).asLinkToPage().getLinkToPage().getDatabaseId()));
  }

  private static String plainText(List<RichText> richText) {
    assertNotNull(richText, "Caption should be returned by the API");
    return richText.stream().map(RichText::getPlainText).collect(Collectors.joining());
  }

  private static String withoutDashes(String id) {
    return id.replace("-", "");
  }
}
