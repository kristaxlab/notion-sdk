package io.kristixlab.notion.api.examples;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kristixlab.notion.api.NotionApiClient;
import io.kristixlab.notion.api.endpoints.impl.PagesEndpointImpl;
import io.kristixlab.notion.api.http.NotionHttpTransport;
import io.kristixlab.notion.api.json.JsonConverter;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.pages.CreatePageParams;
import io.kristixlab.notion.api.model.pages.Page;
import io.kristixlab.notion.api.model.pages.properties.NumberProperty;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration test for PagesApi that calls actual Notion API endpoints. Stores all responses to
 * files for inspection.
 */
public class PagesEndpointImplIntegrationExample {

  private static final String TEST_OUTPUT_DIR = "src/test/resources/notion-api-responses/";

  private PagesEndpointImpl pagesApi;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() throws IOException {
    String token = System.getenv("NOTION_PRIV_INTEGRATION_TOKEN");
    NotionApiClient notionApiClient = new NotionApiClient(token);

    NotionHttpTransport transport = new NotionHttpTransport(notionApiClient.getAuthSettings());
    pagesApi = new PagesEndpointImpl(transport);
    objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

    // Create output directory if it doesn't exist
    Path outputDir = Paths.get(TEST_OUTPUT_DIR);
    Files.createDirectories(outputDir);
  }

  @Test
  void createPage() throws IOException {
    CreatePageParams page = new CreatePageParams();
    NumberProperty number = new NumberProperty();
    number.setNumber(7.77);
    page.setProperties(new HashMap<>());
    page.getProperties().put("Priori", number);
    page.setParent(Parent.datasourceParent("264c5b96-8ec4-8055-8b51-000b4a80c6cc"));
    saveToFile(page, "page-create-request.json");

    Page pageRs = pagesApi.create(page);
    saveToFile(pageRs, "page-create-response.json");
  }

  /** Helper method to save API responses to JSON files. */
  private void saveToFile(Object response, String filename) throws IOException {
    File outputFile = new File(TEST_OUTPUT_DIR + filename);
    JsonConverter.getInstance().toFile(outputFile, response);
    System.out.println("Response saved to: " + outputFile.getAbsolutePath());
  }
}
