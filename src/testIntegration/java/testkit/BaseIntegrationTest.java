package testkit;

import io.kristaxlab.notion.NotionClient;
import org.junit.jupiter.api.BeforeEach;
import testkit.ext.TestPage;
import testkit.ext.client.NotionTestClient;

/**
 * Provides common setup for tests tests that use a {@link NotionClient}.
 *
 * <p>Before each test, this base class creates a client configured to write HTTP exchange logs
 * under a deterministic directory derived from the test class and method names.
 *
 * <p>Subclasses that need a dedicated Notion page for their fixtures declare a static {@code
 * String} field annotated with {@link TestPage}; the page is created before {@code @BeforeAll} runs
 * and a convenience link is logged after every test method.
 */
public abstract class BaseIntegrationTest {

  private NotionClient notionEnvSetupClient;
  private NotionClient notionClient;

  /** Initializes the tests test client */
  @BeforeEach
  protected void beforeEach(
      @NotionTestClient NotionClient client,
      @NotionTestClient(forSetup = true) NotionClient envSetupClient) {
    notionClient = client;
    notionEnvSetupClient = envSetupClient;
  }

  /**
   * Returns the client configured for the current tests test. This client logs all the requests /
   * responses
   *
   * @return the initialized {@link NotionClient} instance
   */
  public NotionClient getNotionClient() {
    return notionClient;
  }

  public NotionClient getSetupClient() {
    return notionEnvSetupClient;
  }
}
