package tests.blocks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.kristaxlab.notion.fluent.NotionBlocks;
import io.kristaxlab.notion.model.block.Block;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.block.BlockType;
import io.kristaxlab.notion.model.block.ImageBlock;
import io.kristaxlab.notion.model.common.FileData;
import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;
import testkit.util.FileLoader;

public class IT12_Blocks_UploadedImage extends WithEmptyTestPage {

  private static final String IMAGE_PATH = "files/it-23/image.jpg";
  private static final String IMAGE_NAME = "image.jpg";
  private static final String CAPTION = "An image created from an uploaded file";

  private String fileUploadId;

  @BeforeEach
  public void setup() {
    fileUploadId = FileLoader.uploadFile(IMAGE_PATH, IMAGE_NAME, getSetupClient());
  }

  @Test
  @DisplayName("IT-12: Blocks - Insert an uploaded file as an image")
  public void testInsertUploadedFileAsImage() {
    ImageBlock imageBlock =
        NotionBlocks.image(image -> image.fileUpload(fileUploadId).caption(CAPTION));

    BlockList appended = getNotionClient().blocks().appendChildren(getTestPageId(), imageBlock);

    assertEquals(1, appended.getResults().size());

    Block appendedBlock = appended.getResults().get(0);
    assertEquals(BlockType.IMAGE.getValue(), appendedBlock.getType());

    // Notion hosts the uploaded file itself, so the block refers to it as a "file", not "external"
    FileData image = appendedBlock.asImage().getImage();
    assertEquals("file", image.getType());
    assertNotNull(image.getFile(), "Image block should expose the Notion hosted file");
    assertNotNull(image.getFile().getUrl(), "Notion hosted file should have a URL");
    assertEquals(CAPTION, plainCaption(image));
  }

  private static String plainCaption(FileData image) {
    assertNotNull(image.getCaption(), "Image caption should be returned by the API");
    return image.getCaption().stream().map(RichText::getPlainText).collect(Collectors.joining());
  }
}
