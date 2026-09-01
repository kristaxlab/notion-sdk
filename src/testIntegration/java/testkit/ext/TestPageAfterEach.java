package testkit.ext;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testkit.util.NotionPageUrlResolver;

/**
 * Logs test page URL after each test execution and clears the current page from the test session.
 * The URL is constructed using the base URL from the configuration parameter
 */
public class TestPageAfterEach implements AfterEachCallback {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestPageAfterEach.class);

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    String currentTestPageId = TestSession.getCurrentPage();
    if (currentTestPageId == null) {
      return;
    }

    String testPageUrl = toNotionPageUrl(context, currentTestPageId);
    LOGGER.info("Completed {}. Notion test page: {}", context.getDisplayName(), testPageUrl);

    TestSession.clearCurrentPage();
  }

  public static String toNotionPageUrl(ExtensionContext context, String pageId) {
    TestSessionConfig config =
        context
            .getStore(ExtensionContext.Namespace.GLOBAL)
            .get("session-config", TestSessionConfig.class);

    String baseUrl = config.getNotionBaseUrl();
    return NotionPageUrlResolver.resolveNotionPageUrl(baseUrl, pageId);
  }
}
