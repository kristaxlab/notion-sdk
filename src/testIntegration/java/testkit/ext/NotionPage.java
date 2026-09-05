package testkit.ext;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testkit.util.NotionPageUrlResolver;

public class NotionPage implements ExtensionContext.Store.CloseableResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(NotionPage.class);

  private final String testId;
  private final String pageId;
  private final String notionBaseUrl;

  public NotionPage(String testId, String pageId, String notionBaseUrl) {
    this.testId = testId;
    this.pageId = pageId;
    this.notionBaseUrl = notionBaseUrl;
  }

  /** Stores this page on the test class store so {@link #close()} logs its URL. */
  public static void register(ExtensionContext context, String testId, String pageId) {
    ExtensionContext.Namespace classNamespace =
        ExtensionContext.Namespace.create(context.getRequiredTestClass());
    String notionBaseUrl =
        NotionPageUrlResolver.getNotionBaseUrl(context); // ensure base URL is resolved and logged
    context.getStore(classNamespace).put(testId, new NotionPage(testId, pageId, notionBaseUrl));
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
