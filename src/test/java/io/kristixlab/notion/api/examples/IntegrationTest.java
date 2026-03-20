package io.kristixlab.notion.api.examples;

import io.kristixlab.notion.api.NotionApiClient;
import io.kristixlab.notion.api.NotionAuthSettings;
import io.kristixlab.notion.api.http.NotionHttpTransport;
import io.kristixlab.notion.api.json.JsonConverter;
import io.kristixlab.notion.api.model.BaseTest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

@Tag("integration")
public class IntegrationTest extends BaseTest {

  private static final String TEST_OUTPUT_DIR = "src/test/resources/notion-api-integration-tests/";
  private static final String TEST_INPUT_DIR = "notion-api-integration-tests/";
  private static final String TOKEN = "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";

  private static NotionHttpTransport transport;

  @BeforeEach
  protected void setUp() throws Exception {
    Path outputDir = Paths.get(TEST_OUTPUT_DIR);
    Files.createDirectories(outputDir);
    String token = "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";
    NotionApiClient notionApiClient = new NotionApiClient(TOKEN);
    // NotionClient notionClient = new NotionClient(token, null);
    NotionAuthSettings authSettings = new NotionAuthSettings();
    authSettings.setAccessToken(token);
    transport = new NotionHttpTransport(authSettings);
  }

  protected static String getTestOutputDir() {
    return TEST_OUTPUT_DIR;
  }

  protected static String getTestInputDir() {
    return TEST_INPUT_DIR;
  }

  protected static NotionHttpTransport getTransport() {
    return transport;
  }

  protected void saveToFile(Object object, String filename) throws IOException {
    JsonConverter.getInstance().toFile(new File(getTestOutputDir() + filename), object);
  }
}
