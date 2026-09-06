package tests.fileuploads;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristaxlab.notion.model.file.FileUploadList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.BaseIntegrationTest;
import testkit.util.FileLoader;

public class IT6_FileUploads_ListExpired extends BaseIntegrationTest {

  private static final String EXPIRED = "expired";
  private static final String UPLOADED = "uploaded";

  private static final String IMAGE_PATH = "files/image_357kb.jpg";
  private static final String UPLOADED_FILENAME = "it-6-image.jpg";

  /** A completed upload gives the filter something it must not return. */
  private String uploadedFileId;

  @BeforeEach
  public void setup() {
    uploadedFileId = FileLoader.uploadFile(IMAGE_PATH, UPLOADED_FILENAME, getSetupClient());
  }

  @Test
  @DisplayName("IT-6: File Uploads - List all expired file uploads")
  public void testListExpiredFileUploads() {
    FileUploadList expired = getNotionClient().fileUploads().listFileUploads(EXPIRED);

    assertNotNull(expired);
    assertNotNull(expired.getResults());
    expired.getResults().forEach(upload -> assertEquals(EXPIRED, upload.getStatus()));

    // an upload that has already received its content never expires
    assertTrue(
        expired.getResults().stream().noneMatch(upload -> uploadedFileId.equals(upload.getId())),
        "A completed upload should not be reported as expired");

    // the same filter applied to the other status returns that upload
    FileUploadList uploaded = getNotionClient().fileUploads().listFileUploads(UPLOADED);

    assertNotNull(uploaded.getResults());
    uploaded.getResults().forEach(upload -> assertEquals(UPLOADED, upload.getStatus()));
    assertTrue(
        uploaded.getResults().stream().anyMatch(upload -> uploadedFileId.equals(upload.getId())),
        "A completed upload should be listed under the 'uploaded' status");
  }
}
