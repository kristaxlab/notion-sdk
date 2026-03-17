package integration;

import integration.util.TestExchangeLogger;
import io.kristixlab.notion.NotionSdkSettings;
import io.kristixlab.notion.api.NotionApiClient;

public class NotionClientProvider {

  public static NotionApiClient internalTestingClient() {
    // TODO env var name? change? move to settings?
    String apiKey = System.getenv("TESTING_BOT_TOKEN");
    if (apiKey == null || apiKey.isEmpty()) {
      throw new IllegalStateException("TESTING_BOT_TOKEN environment variable is not set");
    }

    String exchangeLogsDir =
        NotionSdkSettings.getInstance()
            .getString("notion.api.save-exchanges.base-dir", "test-logs/");

    boolean bodyOnly =
        NotionSdkSettings.getInstance().getBoolean("notion.api.save-exchanges.body-only", false);

    return NotionApiClient.builder()
        .authToken(apiKey)
        .exchangeLogger(new TestExchangeLogger(exchangeLogsDir, bodyOnly))
        .build();
  }
}
