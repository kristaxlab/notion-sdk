package integration.fileuploads;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.kristaxlab.notion.model.file.FileUpload;
import io.kristaxlab.notion.model.file.FileUploadCreateParams;
import io.kristaxlab.notion.model.file.FileUploadSendParams;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.BaseIntegrationTest;
import testkit.util.FileLoader;

public class IT15_FileUploads_SinglePartStream extends BaseIntegrationTest {

  private static final String IMAGE_PATH = "files/image_357kb.jpg";
  private static final String UPLOADED_FILENAME = "it-15-image.jpg";

  private File imageFile;

  @BeforeEach
  public void setup() {
    imageFile = FileLoader.loadFileFailIfMissing(IMAGE_PATH, getClass().getClassLoader());
  }

  @Test
  @DisplayName("IT-15: File Uploads - Upload a single-part file as input stream")
  public void testUploadSinglePartAsStream() throws IOException {
    // 1. Create the file upload
    FileUpload created =
        getNotionClient()
            .fileUploads()
            .create(FileUploadCreateParams.singlePart(UPLOADED_FILENAME));

    assertEquals("pending", created.getStatus());

    // 2. Send the file content read from a stream
    try (InputStream content = new FileInputStream(imageFile)) {
      FileUpload uploaded =
          getNotionClient()
              .fileUploads()
              .upload(created.getId(), FileUploadSendParams.of(content, created.getContentType()));

      assertEquals(created.getId(), uploaded.getId());
      assertEquals("uploaded", uploaded.getStatus());
      assertEquals(UPLOADED_FILENAME, uploaded.getFilename());
      assertEquals(created.getContentType(), uploaded.getContentType());
      assertNotNull(uploaded.getContentLength());
      assertEquals(imageFile.length(), uploaded.getContentLength().longValue());
    }
  }
}
