package testkit.ext;

import io.kristaxlab.notion.NotionClient;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testkit.util.NotionPageUrlResolver;

/**
 * Executes all completion actions after the entire test session finishes.
 *
 * <p>This is the single entry point for session-end logic. It is automatically invoked by JUnit
 * when all tests complete, via the {@link ExtensionContext.Store.CloseableResource} mechanism.
 *
 * <p>Current completion actions:
 *
 * <ul>
 *   <li>Log the session page ID and URL for manual inspection
 *   <li>Optionally delete the session page (when cleanup is enabled)
 * </ul>
 *
 * <p>Future completion actions may include:
 *
 * <ul>
 *   <li>Upload test results to the session page
 *   <li>Generate and attach test reports
 *   <li>Update session page properties with test statistics
 * </ul>
 *
 * <p>This class is registered in the global extension store during session initialization by {@link
 * TestSessionBeforeAll}.
 */
public class TestSessionFinalizer implements ExtensionContext.Store.CloseableResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestSessionFinalizer.class);

  private final NotionClient notionClient;
  private final String sessionPageId;
  private final String notionBaseUrl;
  private final boolean cleanupEnabled;

  /**
   * Creates a session completer with the specified configuration.
   *
   * @param notionClient the Notion client for API operations
   * @param sessionPageId the ID of the session page
   * @param notionBaseUrl the base URL for logging the Notion page link
   * @param cleanupEnabled whether to delete the session page on completion
   */
  public TestSessionFinalizer(
      NotionClient notionClient,
      String sessionPageId,
      String notionBaseUrl,
      boolean cleanupEnabled) {
    this.notionClient = notionClient;
    this.sessionPageId = sessionPageId;
    this.notionBaseUrl = notionBaseUrl;
    this.cleanupEnabled = cleanupEnabled;
  }

  @Override
  public void close() {
    logCompletion();

    if (cleanupEnabled) {
      deleteSessionPage();
    }
  }

  private void logCompletion() {
    String url = NotionPageUrlResolver.resolveNotionPageUrl(notionBaseUrl, sessionPageId);
    LOGGER.info("Test session completed. Session page: {}", url);
  }

  private void deleteSessionPage() {
    LOGGER.info("Cleaning up: moving session page {} to trash", sessionPageId);

    try {
      notionClient.pages().moveToTrash(sessionPageId);
      LOGGER.info("Successfully deleted session page {}", sessionPageId);
    } catch (Exception e) {
      LOGGER.error("Failed to delete session page {}: {}", sessionPageId, e.getMessage(), e);
    }
  }
}
