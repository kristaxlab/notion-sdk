package testkit;

import io.kristaxlab.notion.NotionClient;
import org.junit.jupiter.api.BeforeEach;
import testkit.ext.FixtureNotionPageId;
import testkit.ext.NotionPageId;
import testkit.ext.SessionUserId;
import testkit.ext.client.NotionTestClient;

/**
 * Provides the Notion Test Http Client and setup client. Page and session-user prerequisites are
 * injected by their own annotations ({@link NotionPageId}, {@link FixtureNotionPageId}, {@link
 * SessionUserId}).
 */
public abstract class BaseIntegrationTest {

  private NotionClient notionEnvSetupClient;
  private NotionClient notionClient;

  /**
   * Initializes the tests test client
   */
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
