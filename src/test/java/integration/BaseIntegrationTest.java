package integration;

import integration.extension.NotionTestPage;
import integration.extension.NotionTestPageExtension;
import integration.helper.NotionTestClientProvider;
import io.kristaxlab.notion.NotionClient;
import io.kristaxlab.notion.model.file.FileUpload;
import io.kristaxlab.notion.model.file.FileUploadCreateParams;
import io.kristaxlab.notion.model.file.FileUploadSendParams;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Provides common setup for integration tests that use a {@link NotionClient}.
 *
 * <p>Before each test, this base class creates a client configured to write HTTP exchange logs
 * under a deterministic directory derived from the test class and method names.
 *
 * <p>Subclasses that need a dedicated Notion page for their fixtures declare a static {@code
 * String} field annotated with {@link NotionTestPage}; the page is created before
 * {@code @BeforeAll} runs and a convenience link is logged after every test method.
 */
@Tag("integration")
@ExtendWith({NotionTestPageExtension.class, NotionTstPageLogExtension.class})
public abstract class BaseIntegrationTest {

  @NotionTestPage private static String testPageId;

  private NotionClient notionClient;

  /**
   * Initializes the integration test client with a per-test exchange log directory.
   *
   * @param testInfo metadata for the currently executing test, used to derive log directory names
   */
  @BeforeEach
  protected void beforeEach(TestInfo testInfo) {
    String testClass = testInfo.getTestClass().map(Class::getSimpleName).orElse("unknownClass");

    String testMethod = sanitize(testInfo.getDisplayName());
    if (testMethod.isEmpty()) {
      testMethod = testInfo.getTestMethod().map(Method::getName).orElse("unknownMethod");
    }

    Path exchangeDir = Paths.get("exchanges", "exchange-logs", testClass, testMethod);

    notionClient = NotionTestClientProvider.internalTestingClient(exchangeDir, "Notion Client");
  }

  /**
   * Converts a candidate file or directory name into a filesystem-safe identifier.
   *
   * <p>Illegal path characters are replaced with underscores, whitespace and dot runs are
   * normalized to a single underscore, repeated underscores are collapsed, and leading/trailing
   * underscores are removed.
   *
   * @param name input value to sanitize
   * @return sanitized identifier, or an empty string when {@code name} is {@code null}
   */
  private static String sanitize(String name) {
    if (name == null) return "";
    return name.replaceAll("[/\\\\:*?\"<>|()\\[\\]]", "_")
        .replaceAll("[\\s.]+", "_")
        .replaceAll("_+", "_")
        .replaceAll("^_|_$", "");
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

  protected String getTestPageId() {
    return testPageId;
  }
}
