package integration;

import io.kristixlab.notion.api.NotionApiClient;

public class NotionClientProvider {

  public static NotionApiClient internalTestingClient() {
    // TODO env var name? change? move to settings?
    String apiKey = System.getenv("TESTING_BOT_TOKEN");
    if (apiKey == null || apiKey.isEmpty()) {
      throw new IllegalStateException("TESTING_BOT_TOKEN environment variable is not set");
    }
    return new NotionApiClient(apiKey);
  }
}
