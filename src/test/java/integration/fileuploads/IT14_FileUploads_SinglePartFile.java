package integration.fileuploads;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import integration.BaseIntegrationTest;
import integration.helper.IntegrationTestAssisstant;
import io.kristaxlab.notion.model.file.FileUpload;
import io.kristaxlab.notion.model.file.FileUploadCreateParams;
import io.kristaxlab.notion.model.file.FileUploadSendParams;
import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IT14_FileUploads_SinglePartFile extends BaseIntegrationTest {

  private static final String IMAGE_PATH = "files/image_357kb.jpg";
  private static final String UPLOADED_FILENAME = "it-14-image.jpg";
  private static final String EXPECTED_CONTENT_TYPE = "image/jpeg";

  private File imageFile;

  @BeforeEach
  public void setup() {
    imageFile =
        IntegrationTestAssisstant.loadFileFailIfMissing(IMAGE_PATH, getClass().getClassLoader());
  }

  @Test
  @DisplayName("IT-14: File Uploads - Upload a single-part file")
  public void testUploadSinglePartFile() {
    // 1. Create the file upload - Notion derives the content type from the file name
    FileUpload created =
        getNotionClient()
            .fileUploads()
            .create(FileUploadCreateParams.singlePart(UPLOADED_FILENAME));

    assertNotNull(created.getId());
    assertEquals("pending", created.getStatus());
    assertEquals(UPLOADED_FILENAME, created.getFilename());
    assertEquals(EXPECTED_CONTENT_TYPE, created.getContentType());
    assertNotNull(created.getUploadUrl(), "A pending upload should tell where to send content");

    // 2. Retrieve the upload while it is still waiting for the content
    FileUpload retrieved = getNotionClient().fileUploads().retrieve(created.getId());

    assertEquals(created.getId(), retrieved.getId());
    assertEquals("pending", retrieved.getStatus());
    assertEquals(UPLOADED_FILENAME, retrieved.getFilename());

    // 3. Send the file content
    FileUpload uploaded =
        getNotionClient()
            .fileUploads()
            .upload(created.getId(), FileUploadSendParams.of(imageFile, created.getContentType()));

    assertEquals(created.getId(), uploaded.getId());
    assertEquals("uploaded", uploaded.getStatus());
    assertEquals(UPLOADED_FILENAME, uploaded.getFilename());
    assertEquals(EXPECTED_CONTENT_TYPE, uploaded.getContentType());
    assertNotNull(uploaded.getContentLength());
    assertEquals(imageFile.length(), uploaded.getContentLength().longValue());
  }
}
