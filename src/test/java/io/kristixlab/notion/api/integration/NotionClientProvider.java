package io.kristixlab.notion.api.integration;

import io.kristixlab.notion.api.NotionApiClient;

public class NotionClientProvider {

  public static NotionApiClient internalTestingClient() {
    String apiKey = System.getenv("INTERNAL_TESTING_CLIENT_TOKEN");
    if (apiKey == null || apiKey.isEmpty()) {
      throw new IllegalStateException(
          "INTERNAL_TESTING_CLIENT_TOKEN environment variable is not set");
    }
    return new NotionApiClient(apiKey);
  }
}
