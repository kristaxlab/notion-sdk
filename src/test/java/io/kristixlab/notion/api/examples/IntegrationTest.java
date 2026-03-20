package io.kristixlab.notion.api.examples;

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

@Tag("examples")
public class IntegrationTest extends BaseTest {

  private static final String TEST_OUTPUT_DIR = "src/test/resources/notion-api-integration-tests/";
  private static final String TEST_INPUT_DIR = "notion-api-integration-tests/";

  private static NotionHttpTransport transport;

  @BeforeEach
  protected void setUp() throws Exception {
    Path outputDir = Paths.get(TEST_OUTPUT_DIR);
    Files.createDirectories(outputDir);
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
