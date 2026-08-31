package testkit.ext;

import io.kristaxlab.notion.NotionClient;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testkit.ext.client.NotionTestClientProvisioner;

/**
 * Provisions the Notion test session: creates a dedicated "home" Notion page all tests of the
 * current run work under and initializes session data for test access.
 *
 * <p>The session is provisioned once per test run, no matter how many test classes register this
 * extension: the context is kept in the root {@link ExtensionContext.Store}, whose atomic {@code
 * getOrComputeIfAbsent} also makes concurrent {@code beforeAll} calls of parallel test classes wait
 * for the ongoing initialization. When the whole run is over, the store closes the context, which
 * (optionally) cleans the session page up.
 *
 * <p>The session page is created from a template (see {@link TestSession.Config}) which may contain
 * prefilled pages named after test ids (ex. IT-123) - child pages or database rows. Those are
 * collected and made available via {@link TestSession.Data#getFixturePages()}. This allows tests to
 * run against prerequisites that are impossible to set up through the API. Per-test page resolution
 * is performed by {@link TestPagesProvisioner}.
 *
 * <p>Configuration is resolved via {@link TestSession.Config#from(ExtensionContext)}, which reads
 * from environment variables, system properties, and JUnit properties.
 *
 * <p>This class follows the Single Responsibility Principle by focusing solely on JUnit lifecycle
 * integration. Actual session provisioning is delegated to {@link TestSessionPageProvisioner}.
 */
public class TestSessionBeforeAll implements BeforeAllCallback {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestSessionBeforeAll.class);

  private final TestSessionPageProvisioner provisioner;
  private final NotionClient notionClient;

  /**
   * Default constructor for JUnit extension autodetection.
   *
   * <p>Creates a provisioner with production dependencies.
   */
  public TestSessionBeforeAll() {
    this.notionClient = NotionTestClientProvisioner.getInfraSetupClient();
    this.provisioner =
        new TestSessionPageProvisioner(notionClient, new FixturePagesDiscoverer(notionClient));
  }

  /**
   * Test constructor for dependency injection.
   *
   * <p>Allows tests to inject mocks and verify behavior without hitting the Notion API.
   *
   * @param provisioner the session provisioner to use
   * @param notionClient the Notion client to use
   */
  TestSessionBeforeAll(TestSessionPageProvisioner provisioner, NotionClient notionClient) {
    this.provisioner = provisioner;
    this.notionClient = notionClient;
  }

  @Override
  public void beforeAll(ExtensionContext context) {
    context
        .getRoot()
        .getStore(ExtensionContext.Namespace.GLOBAL)
        .getOrComputeIfAbsent(
            TestSession.Data.class, key -> initializeSession(context), TestSession.Data.class);
  }

  private TestSession.Data initializeSession(ExtensionContext context) {
    try {
      LOGGER.debug("Initializing test session");

      TestSessionConfig config = TestSessionConfig.from(context);
      saveSessionConfig(context, config);

      TestSession.Data sessionData = provisioner.provision(config);
      TestSession.initialize(sessionData);

      registerSessionFinalizer(context, sessionData.getSessionPageId(), config);

      return sessionData;
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      NotionWorkspaseException failure =
          new NotionWorkspaseException("Interrupted while provisioning the Notion test session", e);
      TestSession.failInitialization(failure);
      throw failure;
    } catch (RuntimeException e) {
      TestSession.failInitialization(e);
      throw e;
    }
  }

  private void saveSessionConfig(ExtensionContext context, TestSessionConfig config) {
    LOGGER.debug("Saving Test Session Config {}", config);

    context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL).put("session-config", config);
  }

  /**
   * Registers a session finalizer in the global extension store.
   *
   * <p>The finalizer is responsible for logging the session completion and optionally cleaning up
   * the session page when the test run is over.
   *
   * @param context the extension context
   * @param sessionPageId the ID of the session page
   * @param config the session configuration
   */
  private void registerSessionFinalizer(
      ExtensionContext context, String sessionPageId, TestSessionConfig config) {
    LOGGER.debug("Registering Test Session Finalizer");

    String baseUrl = config.getNotionBaseUrl();
    boolean cleanupEnabled = config.isCleanupEnabled();

    context
        .getRoot()
        .getStore(ExtensionContext.Namespace.GLOBAL)
        .put(
            "session-finalizer",
            new TestSessionFinalizer(notionClient, sessionPageId, baseUrl, cleanupEnabled));
  }
}
