package testkit;

import io.kristaxlab.notion.NotionClient;
import io.kristaxlab.notion.model.file.FileUpload;
import io.kristaxlab.notion.model.file.FileUploadCreateParams;
import io.kristaxlab.notion.model.file.FileUploadSendParams;
import java.io.File;
import java.net.URL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import testkit.ext.TestPage;
import testkit.ext.client.NotionTestClient;
import testkit.ext.client.NotionTestClientProvider;

/**
 * Provides common setup for integration tests that use a {@link NotionClient}.
 *
 * <p>Before each test, this base class creates a client configured to write HTTP exchange logs
 * under a deterministic directory derived from the test class and method names.
 *
 * <p>Subclasses that need a dedicated Notion page for their fixtures declare a static {@code
 * String} field annotated with {@link TestPage}; the page is created before {@code @BeforeAll} runs
 * and a convenience link is logged after every test method.
 */
@Tag("integration")
public abstract class BaseIntegrationTest {

  private NotionClient notionClient;

  /** Initializes the integration test client */
  @BeforeEach
  protected void beforeEach(@NotionTestClient(logExchanges = true) NotionClient client) {
    notionClient = client;
  }

  /**
   * Returns the client configured for the current integration test. This client logs all the
   * requests / responses
   *
   * @return the initialized {@link NotionClient} instance
   */
  public NotionClient getNotionClient() {
    return notionClient;
  }

  public static NotionClient getSetupClient() {
    return NotionTestClientProvider.getInfraSetupClient();
  }

  protected static String uploadFile(String filePath, String fileName) {
    URL url = BaseIntegrationTest.class.getClassLoader().getResource(filePath);
    if (url == null) {
      throw new IllegalStateException(
          String.format("File %s should exist to proceed with the test", filePath));
    }

    File file = new File(url.getFile());
    FileUpload fu =
        getSetupClient().fileUploads().create(FileUploadCreateParams.singlePart(fileName));
    getSetupClient()
        .fileUploads()
        .upload(
            fu.getId(),
            FileUploadSendParams.builder().file(file).contentType(fu.getContentType()).build());
    return fu.getId();
  }
}
