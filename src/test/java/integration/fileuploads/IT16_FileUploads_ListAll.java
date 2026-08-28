package integration.fileuploads;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.kristaxlab.notion.model.file.FileUpload;
import io.kristaxlab.notion.model.file.FileUploadList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.BaseIntegrationTest;

public class IT16_FileUploads_ListAll extends BaseIntegrationTest {

  private static final String IMAGE_PATH = "files/image_357kb.jpg";
  private static final String UPLOADED_FILENAME = "it-16-image.jpg";

  private String uploadedFileId;

  /** The listing is only meaningful when at least one upload belongs to the integration. */
  @BeforeEach
  public void setup() {
    uploadedFileId = uploadFile(IMAGE_PATH, UPLOADED_FILENAME);
  }

  @Test
  @DisplayName("IT-16: File Uploads - List all file uploads")
  public void testListAllFileUploads() {
    FileUploadList uploads = getNotionClient().fileUploads().listFileUploads();

    assertNotNull(uploads);
    assertNotNull(uploads.getResults());
    assertFalse(uploads.getResults().isEmpty());
    uploads
        .getResults()
        .forEach(
            upload -> {
              assertNotNull(upload.getId());
              assertNotNull(upload.getStatus());
            });

    // uploads are listed newest first, so the one created for this test is on the first page
    FileUpload justUploaded =
        uploads.getResults().stream()
            .filter(upload -> uploadedFileId.equals(upload.getId()))
            .findFirst()
            .orElse(null);

    assertNotNull(justUploaded, "The upload created for this test should be listed");
    assertEquals(UPLOADED_FILENAME, justUploaded.getFilename());
    assertEquals("uploaded", justUploaded.getStatus());
  }
}
