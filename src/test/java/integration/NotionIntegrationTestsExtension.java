package integration;

import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs a link to the associated Notion test page after each integration test when one has been
 * registered for the test class.
 *
 * <p>Call {@link #register(Class, String)} from a {@code @BeforeAll} method to associate a Notion
 * page ID with the test class. The link is printed to the log after every {@code @Test} method in
 * that class and the mapping is discarded automatically after the last test in the class completes.
 */
public final class NotionIntegrationTestsExtension implements AfterEachCallback, AfterAllCallback {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(NotionIntegrationTestsExtension.class);

  private static final ConcurrentHashMap<Class<?>, String> PAGE_IDS = new ConcurrentHashMap<>();

  /**
   * Associates a Notion page ID with the given test class so the extension can log a link after
   * each test method. Call this from a {@code @BeforeAll} method.
   *
   * @param testClass the test class performing the registration
   * @param pageId the Notion page ID (with or without hyphens)
   */
  public static void register(Class<?> testClass, String pageId) {
    PAGE_IDS.put(testClass, pageId);
  }

  @Override
  public void afterEach(ExtensionContext context) {
    String pageId = PAGE_IDS.get(context.getRequiredTestClass());
    if (pageId == null || pageId.isBlank()) {
      return;
    }

    LOGGER.info(
        "Completed {}. Notion test page: {}", context.getDisplayName(), toNotionPageUrl(pageId));
  }

  @Override
  public void afterAll(ExtensionContext context) {
    PAGE_IDS.remove(context.getRequiredTestClass());
  }

  public static String toNotionPageUrl(String pageId) {
    return "https://www.notion.so/" + pageId.replace("-", "");
  }
}
