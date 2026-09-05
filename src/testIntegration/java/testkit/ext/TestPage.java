package testkit.ext;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testkit.util.NotionPageUrlResolver;

public class TestPage implements ExtensionContext.Store.CloseableResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestPage.class);

  private final String testId;
  private final String pageId;
  private final String notionBaseUrl;

  public TestPage(String testId, String pageId, String notionBaseUrl) {
    this.testId = testId;
    this.pageId = pageId;
    this.notionBaseUrl = notionBaseUrl;
  }

  @Override
  public void close() {
    logCompletion();
  }

  private void logCompletion() {
    String url = NotionPageUrlResolver.resolveNotionPageUrl(notionBaseUrl, pageId);
    LOGGER.info("Completed {}. Notion test page: {}", testId, url);
  }
}
