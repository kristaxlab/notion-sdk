package integration;


import io.kristixlab.notion.NotionSdkSettings;
import io.kristixlab.notion.api.NotionApiClient;
import io.kristixlab.notion.api.http.transport.log.ExchangeContext;
import java.lang.reflect.Method;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInfo;

@Tag("integration")
public abstract class BaseIntegrationTest {

  private NotionApiClient notionClient;
  // notion sdk settings are not linked semantically to the test class, so TODO: consider moving to
  // a separate util class if more tests will require it
  private NotionSdkSettings settings;

  @BeforeEach
  protected void beforeEach(TestInfo testInfo) {
    notionClient = NotionClientProvider.internalTestingClient();
    settings = NotionSdkSettings.getInstance();
    ExchangeContext.getCurrent()
        .put("testClass", testInfo.getTestClass().map(Class::getSimpleName).orElse("unknownClass"));
    String testMethod = testInfo.getDisplayName();

    if (testMethod != null) {
      // Sanitize testMethod to be a valid file system name:
      // replace characters that are illegal on common file systems (/, \, :, *, ?, ", <, >, |,
      // parens, brackets)
      // and collapse any runs of whitespace or dots into a single underscore, then trim
      // leading/trailing underscores
      testMethod =
          testMethod
              .replaceAll("[/\\\\:*?\"<>|()\\[\\]]", "_")
              .replaceAll("[\\s.]+", "_")
              .replaceAll("_+", "_")
              .replaceAll("^_|_$", "");
    }
    if (testMethod == null || testMethod.isEmpty()) {
      testMethod = testInfo.getTestMethod().map(Method::getName).orElse("unknownMethod");
    }

    ExchangeContext.getCurrent().put("testMethod", testMethod);
  }

  @AfterEach
  protected void afterEach() {
    // Attach exchange logs to Allure before the context is cleared so that
    // getTestLogDir() can still resolve the per-test subdirectory.
    attachExchangeLogsToReport();

    ExchangeContext.getCurrent().clear("testClass");
    ExchangeContext.getCurrent().clear("testLogsPath");
    ExchangeContext.getCurrent().clear("testMethod");
  }

  /**
   * Zips the HTTP exchange log directory produced during the current test and attaches the archive
   * to the Allure report. No-op when the directory does not exist or contains no log files.
   */
  private void attachExchangeLogsToReport() {
    // TODO finalize report attchments solution
//    if (ExchangeContext.getCurrent().get("testLogsPath") == null) {
//      return;
//    }
//    Path logDir = (Path) ExchangeContext.getCurrent().get("testLogsPath");
//    String testMethod = ExchangeContext.getCurrent().getString("testMethod");
//    String attachmentName =
//        (testMethod != null && !testMethod.isEmpty()) ? testMethod : "exchange-logs";
//    AllureLogAttachUtil.attachDirectoryAsZip(logDir, attachmentName);
  }

  public NotionApiClient getNotion() {
    return notionClient;
  }

  public NotionSdkSettings getSettings() {
    return settings;
  }
}
