package testkit.ext;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the test session lifecycle, configuration, and per-thread state.
 *
 * <p>This class unifies session management responsibilities:
 *
 * <ul>
 *   <li>Thread-safe singleton initialization via {@link CompletableFuture}
 *   <li>Immutable session data storage (page ID, bot user ID, fixture pages)
 *   <li>Configuration resolution from environment/system/JUnit properties
 *   <li>Thread-local current page tracking for parallel test execution
 *   <li>Cleanup - removes root test page when tests complete if set in configuration
 * </ul>
 *
 * <p>The session is initialized once per test run. Parallel tests that request the session while
 * it's being provisioned will block until initialization completes.
 */
public class TestSession {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestSession.class);

  private static final CompletableFuture<Data> INSTANCE = new CompletableFuture<>();
  private static final Duration INIT_TIMEOUT = Duration.ofSeconds(60);
  private static final ThreadLocal<String> currentPageId = new ThreadLocal<>();
  private static final AtomicReference<TestSessionConfig> config = new AtomicReference<>();

  /**
   * Initializes the test session with the provided data.
   *
   * <p>This method should be called exactly once per test run. Subsequent calls will throw.
   *
   * @param data the session data to register
   * @throws IllegalStateException if the session was already initialized
   */
  public static void initialize(Data data) {
    if (!INSTANCE.complete(data)) {
      throw new IllegalStateException("Test session was already initialized");
    }
  }

  /**
   * Marks the session initialization as failed.
   *
   * <p>This allows threads blocked in {@link #get()} to fail immediately instead of waiting for the
   * timeout.
   *
   * @param cause the initialization failure cause
   */
  public static void failInitialization(Throwable cause) {
    INSTANCE.completeExceptionally(cause);
  }

  /**
   * Returns the session data, waiting up to {@link #INIT_TIMEOUT} if needed.
   *
   * <p>This method blocks if the session is still being initialized, making it safe to call from
   * parallel test threads.
   *
   * @return the session data
   * @throws IllegalStateException if initialization fails, times out, or is interrupted
   */
  public static Data get() {
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

  /**
   * Sets the current page ID for the calling thread.
   *
   * <p>Used for tracking which page a test is currently operating on, useful for logging and
   * debugging parallel test execution.
   *
   * @param pageId the page ID to set as current
   */
  public static void setCurrentPage(String pageId) {
    currentPageId.set(pageId);
  }

  /**
   * Gets the current page ID for the calling thread.
   *
   * @return the current page ID, or null if not set
   */
  public static String getCurrentPage() {
    return currentPageId.get();
  }

  /** Clears the current page ID for the calling thread. */
  public static void clearCurrentPage() {
    currentPageId.remove();
  }

  /**
   * Immutable data holder for a test session.
   *
   * <p>Contains all information about a provisioned test session: the session page ID, the bot user
   * that created it, and any prefilled fixture pages discovered in the template.
   */
  public static class Data {

    private final String sessionPageId;
    private final String botUserId;
    private final Map<String, String> fixturePages;

    /**
     * Creates session data with the specified values.
     *
     * @param sessionPageId the ID of the session page
     * @param botUserId the ID of the bot user that created the session
     * @param fixturePages map of test IDs to fixture page IDs (will be copied for immutability)
     */
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
}
