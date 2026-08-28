package integration.fileuploads;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import io.kristaxlab.notion.model.file.FileUpload;
import io.kristaxlab.notion.model.file.FileUploadCreateParams;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.BaseIntegrationTest;

public class IT19_FileUploads_ImportExternal extends BaseIntegrationTest {

  private static final String FILENAME = "spring.svg";
  private static final String EXTERNAL_URL =
      "https://docs.spring.io/spring-ai/reference/_/img/spring-logo.svg";
  private static final String EXPECTED_CONTENT_TYPE = "image/svg+xml";

  @Test
  @DisplayName("IT-19: File Uploads - Import external file")
  public void testImportExternalFile() {
    // 1. Ask Notion to pull the file from an external URL
    FileUpload created =
        getNotionClient()
            .fileUploads()
            .create(FileUploadCreateParams.external(FILENAME, EXTERNAL_URL));

    assertNotNull(created.getId());
    assertEquals(FILENAME, created.getFilename());
    assertEquals(EXPECTED_CONTENT_TYPE, created.getContentType());
    // the import runs on Notion side, so no content has arrived yet
    assertEquals("pending", created.getStatus());

    // 2. Retrieve the upload until Notion reports the import as finished
    FileUpload imported = waitForImport(created.getId());

    assertEquals(created.getId(), imported.getId());
    assertEquals(FILENAME, imported.getFilename());
    assertEquals(EXPECTED_CONTENT_TYPE, imported.getContentType());
    assertNotNull(imported.getContentLength(), "An imported file should have a known size");
    assertNotNull(imported.getFileImportResult());
    assertEquals("success", imported.getFileImportResult().getType());
    assertNotNull(imported.getFileImportResult().getImportedTime());
  }

  private FileUpload waitForImport(String fileUploadId) {
    FileUpload last = null;
    for (int attempt = 0; attempt < 20; attempt++) {
      last = getNotionClient().fileUploads().retrieve(fileUploadId);
      if ("uploaded".equals(last.getStatus())) {
        return last;
      }
      if ("failed".equals(last.getStatus())) {
        fail("Import of " + EXTERNAL_URL + " failed: " + last.getFileImportResult());
      }
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        fail("Interrupted while waiting for the import of " + EXTERNAL_URL);
      }
    }
    fail(
        "Timed out waiting for the import of "
            + EXTERNAL_URL
            + "; last status="
            + last.getStatus());
    return last;
  }
}
