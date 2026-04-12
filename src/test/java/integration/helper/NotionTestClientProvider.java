package integration.helper;

import static io.kristixlab.notion.api.NotionTestEnvironmentConstants.NOTION_TEST_AUTH_TOKEN;

import io.kristixlab.notion.api.NotionClient;
import io.kristixlab.notion.api.http.base.json.TestSerializer;
import java.nio.file.Path;

/**
 * Factory for {@link NotionClient} instances pre-configured for internal integration tests.
 *
 * <p>Every client produced by this class:
 *
 * <ul>
 *   <li>reads the Notion API key from the NOTION_TEST_AUTH_TOKEN environment variable
 *   <li>uses a {@link TestSerializer} – a strict Jackson serializer that fails on unknown JSON
 *       properties, ensuring the SDK's model classes stay in sync with the live Notion API
 * </ul>
 *
 * <p>The environment variable can be supplied directly or via a {@code .env.test.local} file that
 * the Gradle {@code integrationTest} task loads automatically.
 *
 * @see NotionClient#builder()
 * @see TestSerializer
 */
public class NotionTestClientProvider {

  /**
   * Creates a {@link NotionClient} for internal integration tests without exchange logging.
   *
   * <p>Prefer {@link #internalTestingClient(Path)} when you need per-test exchange files written to
   * disk for offline inspection or regression snapshots.
   *
   * @return a fully-wired {@link NotionClient} backed by the NOTION_TEST_AUTH_TOKEN token
   * @throws IllegalStateException if the NOTION_TEST_AUTH_TOKEN environment variable is absent or
   *     blank
   */
  public static NotionClient internalTestingClient() {
    return internalTestingClient(null);
  }

  /**
   * Creates a {@link NotionClient} for internal integration tests.
   *
   * <p>When {@code exchangeLogDir} is non-{@code null}, an {@link
   * io.kristixlab.notion.api.http.base.interceptor.ExchangeRecordingInterceptor} is added to the
   * HTTP pipeline and writes each request/response pair as a pretty-printed JSON file into the
   * given directory. The directory is created automatically if it does not exist.
   *
   * @param exchangeLogDir target directory for HTTP exchange files; {@code null} disables exchange
   *     logging
   * @return a fully-wired {@link NotionClient} backed by the NOTION_TEST_AUTH_TOKENtoken
   * @throws IllegalStateException if the NOTION_TEST_AUTH_TOKEN environment variable is absent or
   *     blank
   */
  public static NotionClient internalTestingClient(Path exchangeLogDir) {
    String apiKey = System.getenv(NOTION_TEST_AUTH_TOKEN);
    if (apiKey == null || apiKey.isEmpty()) {
      throw new IllegalStateException(NOTION_TEST_AUTH_TOKEN + " environment variable is not set");
    }

    return NotionClient.builder()
        .authToken(apiKey)
        .jsonSerializer(new TestSerializer()) // strict serializer
        .exchangeLogging(exchangeLogDir)
        .build();
  }
}
