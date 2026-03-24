package integration;

import io.kristixlab.notion.NotionSdkSettings;
import io.kristixlab.notion.api.NotionClient;

public class NotionClientProvider {

  public static NotionClient internalTestingClient() {
    // TODO env var name? change? move to settings?
    String apiKey = System.getenv("TESTING_BOT_TOKEN");
    if (apiKey == null || apiKey.isEmpty()) {
      throw new IllegalStateException("TESTING_BOT_TOKEN environment variable is not set");
    }

    // TODO: file-based exchange logging (ExchangeLogger / SequentialExchangeLogger) is not yet
    //  supported in the new transport layer. HTTP traffic is currently logged to SLF4J only via
    //  LoggingHttpInterceptor. Implement a FileLoggingHttpInterceptor to restore this capability.
    boolean jsonStrict =
        NotionSdkSettings.getInstance()
            .getBoolean("notion.api.json.fail-on-unknown-properties", false);

    return NotionClient.builder().auth(apiKey).jsonFailOnUnknownProperties(jsonStrict).build();
  }
}
