package integration;

import io.kristixlab.notion.NotionSdkSettings;
import io.kristixlab.notion.api.NotionClient;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInfo;

@Tag("integration")
public abstract class BaseIntegrationTest {

  private NotionClient notionClient;
  // TODO notion sdk settings are not linked semantically to the test class, so TODO: consider
  // moving to
  // a separate util class if more tests will require it
  private NotionSdkSettings settings;

  @BeforeEach
  protected void beforeEach(TestInfo testInfo) {
    settings = NotionSdkSettings.getInstance();

    String testClass = testInfo.getTestClass().map(Class::getSimpleName).orElse("unknownClass");

    String testMethod = sanitize(testInfo.getDisplayName());
    if (testMethod.isEmpty()) {
      testMethod = testInfo.getTestMethod().map(Method::getName).orElse("unknownMethod");
    }

    Path exchangeDir = Paths.get("exchanges", "exchange-logs", testClass, testMethod);

    notionClient = NotionClientProvider.internalTestingClient(exchangeDir);
  }

  /**
   * Strips characters that are illegal on common file systems and collapses whitespace/dot runs
   * into a single underscore.
   */
  private static String sanitize(String name) {
    if (name == null) return "";
    return name.replaceAll("[/\\\\:*?\"<>|()\\[\\]]", "_")
        .replaceAll("[\\s.]+", "_")
        .replaceAll("_+", "_")
        .replaceAll("^_|_$", "");
  }

  @AfterEach
  protected void afterEach() {
    // Attach exchange logs to Allure before the context is cleared so that
    // getTestLogDir() can still resolve the per-test subdirectory.
    attachExchangeLogsToReport();
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

  public NotionClient getNotion() {
    return notionClient;
  }

  public NotionSdkSettings getSettings() {
    return settings;
  }
}
