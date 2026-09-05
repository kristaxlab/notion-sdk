package testkit.ext;

import io.kristaxlab.notion.NotionClient;
import java.util.Map;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testkit.ext.client.NotionTestClientProvisioner;
import testkit.util.NotionPageUrlResolver;

/**
 * Run-scoped store for integration-test prerequisites. Provisioners obtain it with {@link
 * #get(ExtensionContext)}; test classes do not call this type.
 *
 * <p>The instance is a thread-safe singleton on the root {@link ExtensionContext.Store}. JUnit
 * calls {@link #close()} when the store is closed. Each prerequisite (session user id, test session
 * page, fixture pages) is initialized once, on first demand.
 */
public class TestSession implements ExtensionContext.Store.CloseableResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestSession.class);

  private final NotionClient notionClient;
  private final TestSessionPageProvisioner provisioner;
  private final String notionBaseUrl;
  private final boolean cleanupEnabled;

  private String sessionUserId;
  private String testSessionPageId;
  private Map<String, String> fixturePages = Map.of();
  private boolean fixturesDiscovered;

  TestSession(
      NotionClient notionClient,
      TestSessionPageProvisioner provisioner,
      String notionBaseUrl,
      boolean cleanupEnabled) {
    this.notionClient = notionClient;
    this.provisioner = provisioner;
    this.notionBaseUrl = notionBaseUrl;
    this.cleanupEnabled = cleanupEnabled;
  }

  /**
   * Returns the session for this run, creating it if needed. For provisioners only. Safe to call
   * from parallel provisioners — the root store's {@code getOrComputeIfAbsent} publishes one
   * instance.
   */
  public static TestSession get(ExtensionContext context) {
    return context
        .getRoot()
        .getStore(ExtensionContext.Namespace.GLOBAL)
        .getOrComputeIfAbsent(TestSession.class, key -> create(context), TestSession.class);
  }

  private static TestSession create(ExtensionContext context) {
    NotionClient client = NotionTestClientProvisioner.getInfraSetupClient();
    TestSessionPageProvisioner provisioner =
        new TestSessionPageProvisioner(client, new FixturePagesDiscoverer(client));
    return new TestSession(
        client,
        provisioner,
        NotionPageUrlResolver.getNotionBaseUrl(context),
        TestSessionConfig.cleanupEnabled(context));
  }

  /** Resolves {@code users().me()} once and returns the session user id. */
  public synchronized String ensureSessionUserId() {
    if (sessionUserId == null) {
      sessionUserId = notionClient.users().me().getId();
      LOGGER.info("Session user id: {}", sessionUserId);
    }
    return sessionUserId;
  }

  /**
   * Creates the test session page on first use. Does not wait for template content or discover
   * fixture pages.
   *
   * @return the test session page id
   */
  public synchronized String ensureTestSessionPage(ExtensionContext context) {
    if (testSessionPageId != null) {
      return testSessionPageId;
    }
    TestSessionConfig config = TestSessionConfig.from(context);
    testSessionPageId = provisioner.createTestSessionPage(config);
    LOGGER.info(
        "Test session page: {}",
        NotionPageUrlResolver.resolveNotionPageUrl(notionBaseUrl, testSessionPageId));
    return testSessionPageId;
  }

  /**
   * Ensures the test session page, waits for template content, and discovers fixture pages under
   * it.
   *
   * @return test id → fixture page id
   */
  public synchronized Map<String, String> ensureFixtures(ExtensionContext context) {
    if (fixturesDiscovered) {
      return fixturePages;
    }
    String pageId = ensureTestSessionPage(context);
    fixturePages = provisioner.discoverFixtures(pageId);
    fixturesDiscovered = true;
    LOGGER.info(
        "Fixture pages: {} ({} fixture(s))",
        NotionPageUrlResolver.resolveNotionPageUrl(notionBaseUrl, pageId),
        fixturePages.size());
    return fixturePages;
  }

  /** Logs the test session page URL and, if cleanup is on, moves that page to trash. */
  @Override
  public void close() {
    logCompletion();

    if (cleanupEnabled) {
      deletePage(testSessionPageId);
    }
  }

  private void logCompletion() {
    if (testSessionPageId == null) {
      LOGGER.info("Test session completed (no test session page)");
      return;
    }
    LOGGER.info(
        "Test session completed. Test session page: {}",
        NotionPageUrlResolver.resolveNotionPageUrl(notionBaseUrl, testSessionPageId));
  }

  private void deletePage(String pageId) {
    if (pageId == null) {
      return;
    }
    LOGGER.info("Cleaning up: moving test session page {} to trash", pageId);
    try {
      notionClient.pages().moveToTrash(pageId);
      LOGGER.info("Successfully deleted test session page {}", pageId);
    } catch (Exception e) {
      LOGGER.error("Failed to delete test session page {}: {}", pageId, e.getMessage(), e);
    }
  }
}
