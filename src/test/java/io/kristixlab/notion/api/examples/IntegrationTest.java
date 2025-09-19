package io.kristixlab.notion.api.examples;

import io.kristixlab.notion.api.NotionApiClient;
import io.kristixlab.notion.api.NotionApiTransport;
import io.kristixlab.notion.api.model.BaseTest;
import io.kristixlab.notion.api.util.JsonConverter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;

public class IntegrationTest extends BaseTest {

  private static final String TEST_OUTPUT_DIR = "src/test/resources/notion-api-integration-tests/";
  private static final String TEST_INPUT_DIR = "notion-api-integration-tests/";
  private static final String TOKEN = "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";

  private static NotionApiTransport transport;

  @BeforeEach
  protected void setUp() throws Exception {
    Path outputDir = Paths.get(TEST_OUTPUT_DIR);
    Files.createDirectories(outputDir);
    // String token = "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";
    NotionApiClient notionApiClient = new NotionApiClient(TOKEN, null);
    // NotionClient notionClient = new NotionClient(token, null);
    transport = new NotionApiTransport(notionApiClient, "Notion API");
  }

  protected static String getTestOutputDir() {
    return TEST_OUTPUT_DIR;
  }

  protected static String getTestInputDir() {
    return TEST_INPUT_DIR;
  }

  protected static NotionApiTransport getTransport() {
    return transport;
  }

  protected void saveToFile(Object object, String filename) throws IOException {
    JsonConverter.getInstance().toFile(new File(getTestOutputDir() + filename), object);
    /*getObjectMapper()
    .writerWithDefaultPrettyPrinter()
    .writeValue(new File(getTestOutputDir() + filename), object);*/
  }
}
