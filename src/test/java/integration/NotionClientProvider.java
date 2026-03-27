package integration;

import io.kristixlab.notion.NotionSdkSettings;
import io.kristixlab.notion.api.NotionClient;
import util.TestSerializer;

import java.nio.file.Path;

public class NotionClientProvider {

  /**
   * Creates a {@link NotionClient} for internal integration tests without exchange logging. Use
   * {@link #internalTestingClient(Path)} when you need per-test exchange files.
   */
  public static NotionClient internalTestingClient() {
    return internalTestingClient(null);
  }

  /**
   * Creates a {@link NotionClient} for internal integration tests.
   *
   * @param exchangeLogDir when non-{@code null}, each HTTP exchange is written as a JSON file into
   *     this directory; pass {@code null} to disable exchange logging
   */
  public static NotionClient internalTestingClient(Path exchangeLogDir) {
    String apiKey = System.getenv("TESTING_BOT_TOKEN");
    if (apiKey == null || apiKey.isEmpty()) {
      throw new IllegalStateException("TESTING_BOT_TOKEN environment variable is not set");
    }

    boolean jsonStrict =
        NotionSdkSettings.getInstance()
            .getBoolean("notion.api.json.fail-on-unknown-properties", false);

    return NotionClient.builder()
        .auth(apiKey)
        .jsonSerializer(new TestSerializer())
        .exchangeLogging(exchangeLogDir)
        .build();
  }
}
