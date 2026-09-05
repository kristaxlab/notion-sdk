package testkit;

import io.kristaxlab.notion.NotionClient;
import org.junit.jupiter.api.BeforeEach;
import testkit.ext.FixtureNotionPageId;
import testkit.ext.NotionPageId;
import testkit.ext.SessionUserId;
import testkit.ext.client.NotionTestClient;

/**
 * Provides the Notion Test Http Client and the setup client. Page and session-user prerequisites
 * are injected by their own annotations ({@link NotionPageId}, {@link FixtureNotionPageId}, {@link
 * SessionUserId}).
 */
public abstract class BaseIntegrationTest {

  private NotionClient notionEnvSetupClient;
  private NotionClient notionClient;

  /** Injects the Notion Test Http Client and the setup client. */
  @BeforeEach
  protected void beforeEach(
      @NotionTestClient NotionClient client,
      @NotionTestClient(forSetup = true) NotionClient envSetupClient) {
    notionClient = client;
    notionEnvSetupClient = envSetupClient;
  }

  /**
   * Returns the Notion Test Http Client for the call the test is checking.
   *
   * @return the injected {@link NotionClient}
   */
  public NotionClient getNotionClient() {
    return notionClient;
  }

  /**
   * Returns the setup client for arrange-only calls.
   *
   * @return the injected setup {@link NotionClient}
   */
  public NotionClient getSetupClient() {
    return notionEnvSetupClient;
  }
}
