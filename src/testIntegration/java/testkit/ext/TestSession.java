package testkit.ext;

import io.kristaxlab.notion.NotionClient;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testkit.util.NotionPageUrlResolver;

/**
 * The integration-test session: provisioned data for the run, plus suite-end cleanup.
 *
 * <p>One instance is created per JVM run, published through {@link #initialize}, and stored on the
 * root {@link ExtensionContext.Store} so JUnit calls {@link #close()} when the store is closed.
 * Tests and extensions read it via {@link #get()}, which blocks until initialization completes.
 */
public class TestSession implements ExtensionContext.Store.CloseableResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestSession.class);

  private static final CompletableFuture<TestSession> INSTANCE = new CompletableFuture<>();
  private static final Duration INIT_TIMEOUT = Duration.ofSeconds(60);

  private final Data data;
  private final NotionClient notionClient;
  private final String notionBaseUrl;
  private final boolean cleanupEnabled;

  TestSession(Data data, NotionClient notionClient, String notionBaseUrl, boolean cleanupEnabled) {
    this.data = data;
    this.notionClient = notionClient;
    this.notionBaseUrl = notionBaseUrl;
    this.cleanupEnabled = cleanupEnabled;
  }

  /**
   * Registers the session for this run. Must be called exactly once; subsequent calls throw.
   *
   * @return the same instance, so the caller can put it in the JUnit store
   */
  public static TestSession initialize(
      Data data, NotionClient notionClient, String notionBaseUrl, boolean cleanupEnabled) {
    TestSession session = new TestSession(data, notionClient, notionBaseUrl, cleanupEnabled);
    if (!INSTANCE.complete(session)) {
      throw new IllegalStateException("Test session was already initialized");
    }
    return session;
  }

  /**
   * Marks initialization as failed so threads blocked in {@link #get()} fail immediately instead of
   * waiting for the timeout.
   */
  public static void failInitialization(Throwable cause) {
    INSTANCE.completeExceptionally(cause);
  }

  /**
   * Returns the session, waiting up to {@link #INIT_TIMEOUT} if it is still being provisioned.
   *
   * @throws IllegalStateException if initialization fails, times out, or is interrupted
   */
  public static TestSession get() {
    try {
      return INSTANCE.get(INIT_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Interrupted while waiting for session initialization", e);
    } catch (ExecutionException e) {
      throw new IllegalStateException("Session initialization failed", e.getCause());
    } catch (TimeoutException e) {
      throw new IllegalStateException("Session was not initialized within " + INIT_TIMEOUT, e);
    }
  }

  public String getSessionPageId() {
    return data.getSessionPageId();
  }

  public String getBotUserId() {
    return data.getBotUserId();
  }

  public Map<String, String> getFixturePages() {
    return data.getFixturePages();
  }

  /**
   * Immutable snapshot of what was provisioned: session page, bot user, and fixture pages.
   *
   * <p>Add a field here only when several tests need the same extra read.
   */
  public static class Data {

    private final String sessionPageId;
    private final String botUserId;
    private final Map<String, String> fixturePages;

    public Data(String sessionPageId, String botUserId, Map<String, String> fixturePages) {
      this.sessionPageId = sessionPageId;
      this.botUserId = botUserId;
      this.fixturePages = (fixturePages != null) ? Map.copyOf(fixturePages) : Map.of();
    }

    public String getSessionPageId() {
      return sessionPageId;
    }

    public String getBotUserId() {
      return botUserId;
    }

    public Map<String, String> getFixturePages() {
      return fixturePages;
    }
  }

  @Override
  public void close() {
    logCompletion();

    if (cleanupEnabled) {
      deleteSessionPage();
    }
  }

  private void logCompletion() {
    String url = NotionPageUrlResolver.resolveNotionPageUrl(notionBaseUrl, getSessionPageId());
    LOGGER.info("Test session completed. Session page: {}", url);
  }

  private void deleteSessionPage() {
    String sessionPageId = getSessionPageId();
    LOGGER.info("Cleaning up: moving session page {} to trash", sessionPageId);

    try {
      notionClient.pages().moveToTrash(sessionPageId);
      LOGGER.info("Successfully deleted session page {}", sessionPageId);
    } catch (Exception e) {
      LOGGER.error("Failed to delete session page {}: {}", sessionPageId, e.getMessage(), e);
    }
  }
}
