package io.kristixlab.notion.api.examples;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kristixlab.notion.api.NotionApiClient;
import io.kristixlab.notion.api.NotionApiTransport;
import io.kristixlab.notion.api.PagesApi;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.common.RichText;
import io.kristixlab.notion.api.model.pages.Page;
import io.kristixlab.notion.api.model.pages.UpdatePageRequest;
import io.kristixlab.notion.api.model.pages.properties.NumberProperty;
import io.kristixlab.notion.api.model.pages.properties.PageProperty;
import io.kristixlab.notion.api.model.pages.properties.RichTextProperty;
import io.kristixlab.notion.api.model.pages.properties.TitleProperty;
import io.kristixlab.notion.api.util.JsonConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Integration test for PagesApi that calls actual Notion API endpoints. Stores all responses to
 * files for inspection.
 */
public class PagesApiIntegrationExample {

  private static final String PAGE_ID = "24cc5b96-8ec4-809c-9809-caa1d493ca04";
  private static final String TEST_OUTPUT_DIR = "src/test/resources/notion-api-responses/";

  private PagesApi pagesApi;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() throws IOException {
    String token = System.getenv("NOTION_PRIV_INTEGRATION_TOKEN");
    NotionApiClient notionApiClient = new NotionApiClient(token);

    NotionApiTransport transport = new NotionApiTransport(notionApiClient.getAuthSettings());
    pagesApi = new PagesApi(transport);
    objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

    // Create output directory if it doesn't exist
    Path outputDir = Paths.get(TEST_OUTPUT_DIR);
    Files.createDirectories(outputDir);
  }

  @Test
  void createPage() throws IOException {
    Page page = new Page();
    NumberProperty number = new NumberProperty();
    number.setNumber(7.77);
    page.setProperties(new HashMap<>());
    page.getProperties().put("Priori", number);
    page.setParent(Parent.datasourceParent("264c5b96-8ec4-8055-8b51-000b4a80c6cc"));
    saveToFile(page, "page-create-request.json");

    page = pagesApi.create(page);
    saveToFile(page, "page-create-response.json");
  }

  @Test
  void updatePage() throws IOException {
    Page page = new Page();
    NumberProperty number = new NumberProperty();
    number.setNumber(7.77);
    TitleProperty title = new TitleProperty();
    title.setTitle(new ArrayList<>());
    title.getTitle().add(new RichText());
    title.getTitle().get(0).setPlainText("New page " + System.currentTimeMillis());
    title.getTitle().get(0).setText(new RichText.Text());
    title.getTitle().get(0).getText().setContent(title.getTitle().get(0).getPlainText());
    page.setProperties(new HashMap<>());
    page.getProperties().put("Number", number);
    page.setParent(new Parent());
    page.getParent().setDatabaseId("24cc5b96-8ec4-800a-a809-c7f6508f45f2");
    page.getProperties().put("title", title);
    saveToFile(page, "page-create-before-update-request.json");

    page = pagesApi.create(page);

    String id = page.getId();

    UpdatePageRequest updatedPage = new UpdatePageRequest();
    NumberProperty updatedNumber = new NumberProperty();
    updatedNumber.setNumber(4.456);
    updatedPage.setProperties(new HashMap<>());
    updatedPage.getProperties().put("Number", updatedNumber);
    updatedPage.setParent(new Parent());
    updatedPage.getParent().setDatabaseId("24cc5b96-8ec4-800a-a809-c7f6508f45f2");

    RichTextProperty text = new RichTextProperty();
    text.setRichText(new ArrayList<>());
    text.getRichText().add(new RichText());
    text.getRichText().get(0).setPlainText("yay, updated!");
    text.getRichText().get(0).setText(new RichText.Text());
    text.getRichText().get(0).getText().setContent("yay, updated!");
    updatedPage.getProperties().put("Text", text);
    saveToFile(page, "page--update-request.json");
    Page updated = pagesApi.update(id, updatedPage);

    saveToFile(updated, "page-update-response.json");
  }

  @Test
  void retrievePage() throws IOException {
    Page page = pagesApi.retrieve(PAGE_ID);
    saveToFile(page, "page-retrieve-response.json");
  }

  @Test
  void retrieveDatabasePage() throws IOException {
    Page page = pagesApi.retrieve("24cc5b96-8ec4-800a-a809-c7f6508f45f2");
    saveToFile(page, "pages-db-retrieve-response.json");
  }

  @Test
  void testRetrievePageProperty() throws IOException {
    String propertyId = "%3AylC";
    PageProperty retrievedProperty = pagesApi.retrieveProperty(PAGE_ID, "%3AylC");
    saveToFile(retrievedProperty, "page-retrieve-property-rs.json");
  }

  @Test
  void testArchiveAndRestorePage() throws IOException {
    Page archivedPage = pagesApi.delete(PAGE_ID);
    saveToFile(archivedPage, "page-archive-response.json");

    Page unarchivedPage = pagesApi.restore(PAGE_ID);
    saveToFile(unarchivedPage, "page-unarchive-response.json");
  }

  /**
   * Helper method to save API responses to JSON files.
   */
  private void saveToFile(Object response, String filename) throws IOException {
    File outputFile = new File(TEST_OUTPUT_DIR + filename);
    JsonConverter.getInstance().toFile(outputFile, response);
    System.out.println("Response saved to: " + outputFile.getAbsolutePath());
  }
}
