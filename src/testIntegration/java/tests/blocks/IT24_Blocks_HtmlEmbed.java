package tests.blocks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristaxlab.notion.fluent.NotionBlocks;
import io.kristaxlab.notion.model.block.Block;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.block.BlockType;
import io.kristaxlab.notion.model.block.EmbedBlock;
import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;
import testkit.util.FileLoader;

public class IT24_Blocks_HtmlEmbed extends WithEmptyTestPage {

  private static final String HTML_PATH = "files/it-24/widget.html";
  private static final String HTML_NAME = "widget.html";
  private static final String CAPTION = "HTML embed from uploaded file";

  private String fileUploadId;

  @BeforeEach
  public void setup() {
    fileUploadId = FileLoader.uploadFile(HTML_PATH, HTML_NAME, getSetupClient());
  }

  @Test
  @DisplayName("IT-24: Blocks - Append HTML embed via file_upload and retrieve url + caption")
  public void testAppendHtmlEmbedViaFileUpload() {
    EmbedBlock embedBlock = NotionBlocks.embed(e -> e.fileUpload(fileUploadId).caption(CAPTION));

    BlockList appended = getNotionClient().blocks().appendChildren(getTestPageId(), embedBlock);

    assertEquals(1, appended.getResults().size());

    Block appendedBlock = appended.getResults().get(0);
    assertEquals(BlockType.EMBED.getValue(), appendedBlock.getType());

    EmbedBlock.Embed embed = appendedBlock.asEmbed().getEmbed();
    assertNotNull(embed.getUrl(), "HTML embed response should expose a temporary url");
    assertTrue(embed.getUrl().startsWith("http"), "Temporary url should be an http(s) link");
    assertEquals(CAPTION, plainCaption(embed));

    Block retrieved = getNotionClient().blocks().retrieve(appendedBlock.getId());
    EmbedBlock.Embed retrievedEmbed = retrieved.asEmbed().getEmbed();
    assertNotNull(retrievedEmbed.getUrl(), "Retrieve should return a temporary url");
    assertEquals(CAPTION, plainCaption(retrievedEmbed));
  }

  private static String plainCaption(EmbedBlock.Embed embed) {
    assertNotNull(embed.getCaption(), "Embed caption should be returned by the API");
    return embed.getCaption().stream().map(RichText::getPlainText).collect(Collectors.joining());
  }
}
