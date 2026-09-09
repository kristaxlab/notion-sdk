package tests.fileuploads;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.kristaxlab.notion.endpoints.util.FileUploadUtils;
import io.kristaxlab.notion.model.file.FileUpload;
import io.kristaxlab.notion.model.file.FileUploadCreateParams;
import io.kristaxlab.notion.model.file.FileUploadSendParams;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.BaseIntegrationTest;
import testkit.util.FileLoader;

public class IT7_FileUploads_SinglePartBytes extends BaseIntegrationTest {

  private static final String IMAGE_PATH = "files/image_357kb.jpg";
  private static final String UPLOADED_FILENAME = "it-7-image.jpg";

  private byte[] imageContent;

  @BeforeEach
  public void setup() throws IOException {
    imageContent =
        FileUploadUtils.fileToBytes(
            FileLoader.loadFileFailIfMissing(IMAGE_PATH, getClass().getClassLoader()));
  }

  @Test
  @DisplayName("IT-7: File Uploads - Upload a single-part file as byte array")
  public void testUploadSinglePartAsBytes() {
    // 1. Create the file upload
    FileUpload created =
        getNotionClient()
            .fileUploads()
            .create(FileUploadCreateParams.singlePart(UPLOADED_FILENAME));

    assertEquals("pending", created.getStatus());

    // 2. Send the file content as a byte array
    FileUpload uploaded =
        getNotionClient()
            .fileUploads()
            .upload(
                created.getId(), FileUploadSendParams.of(imageContent, created.getContentType()));

    assertEquals(created.getId(), uploaded.getId());
    assertEquals("uploaded", uploaded.getStatus());
    assertEquals(UPLOADED_FILENAME, uploaded.getFilename());
    assertEquals(created.getContentType(), uploaded.getContentType());
    assertNotNull(uploaded.getContentLength());
    assertEquals(imageContent.length, uploaded.getContentLength().longValue());
  }
}
