package testkit.ext;

import io.kristaxlab.notion.NotionClient;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testkit.ext.client.NotionTestClientProvisioner;
import testkit.util.NotionPageUrlResolver;

/**
 * Provisions the Notion test session once per run and stores the {@link TestSession} on the root
 * {@link ExtensionContext.Store}.
 *
 * <p>{@code getOrComputeIfAbsent} makes concurrent {@code beforeAll} calls of parallel test classes
 * wait for the same initialization. When the run ends, the store closes the session, which logs the
 * session URL and optionally moves the session page to trash.
 *
 * <p>The session page is created from a template (see {@link TestSessionConfig}) which may contain
 * prefilled pages named after test ids (ex. IT-123) — child pages or database rows. Those are
 * collected and made available via {@link TestSession#getFixturePages()}. Per-test page resolution
 * is performed by {@link TestPagesProvisioner}.
 *
 * <p>This class only integrates with the JUnit lifecycle. Provisioning is delegated to {@link
 * TestSessionPageProvisioner}.
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
            TestSession.class, key -> initializeSession(context), TestSession.class);
  }

  private TestSession initializeSession(ExtensionContext context) {
    try {
      LOGGER.debug("Initializing test session");

      TestSessionConfig config = TestSessionConfig.from(context);
      TestSession.Data sessionData = provisioner.provision(config);
      String notionBaseUrl = NotionPageUrlResolver.getNotionBaseUrl(context);

      return TestSession.initialize(
          sessionData, notionClient, notionBaseUrl, config.isCleanupEnabled());
    } catch (RuntimeException e) {
      TestSession.failInitialization(e);
      throw e;
    }
  }
}
