package integration;

import static org.junit.jupiter.api.Assertions.*;

import integration.helper.IntegrationTestAssisstant;
import io.kristaxlab.notion.fluent.NotionBlocks;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.UpdatePageParams;
import io.kristaxlab.notion.model.page.property.*;
import io.kristaxlab.notion.model.page.templates.TemplateParams;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PagesIT extends BaseIntegrationTest {

  /** Replace with the id of the prerequisite data source described in the class javadoc. */
  private static final String DATA_SOURCE_ID =
      IntegrationTestAssisstant.getPrerequisites().getTestDatabaseId();

  /** Parent page used by every test that needs a {@code parent: page_id} container. */
  private static String pagesTestRootId;

  @BeforeAll
  public static void setupClass() {
    pagesTestRootId = IntegrationTestAssisstant.createPageForTests("Pages");
  }

  @Test
  @DisplayName("[IT-35]: Pages - Duplicate a page using its own id as template_id")
  public void testDuplicatePageViaTemplateId() {
    Page original =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inPage(pagesTestRootId)
                        .title("[IT-35] Original")
                        .children(c -> c.heading2("Heading").paragraph("Some content")));

    Page duplicate =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inPage(pagesTestRootId)
                        .title("[IT-35] Duplicate")
                        .template(TemplateParams.templateId(original.getId())));

    assertNotNull(duplicate.getId());
    assertNotEquals(original.getId(), duplicate.getId());

    // Notion copies the source content into the duplicate; we only assert that something was
    // copied because Notion sometimes wraps the content asynchronously.
    BlockList children = getNotionClient().blocks().retrieveChildren(duplicate.getId());
    assertNotNull(children);
    assertNotNull(children.getResults());
    assertFalse(
        children.getResults().isEmpty(), "Duplicate page should inherit content from the template");
  }

  @Test
  @DisplayName("[IT-37]: Pages - Apply a template to an existing page (with erase content)")
  public void testApplyTemplateToExistingPage() {
    Page templateSource =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inPage(pagesTestRootId)
                        .title("[IT-37] Template source")
                        .children(
                            c -> c.heading2("Template heading").paragraph("Template content")));

    Page target =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inPage(pagesTestRootId)
                        .title("[IT-37] Target")
                        .children(c -> c.paragraph("Original target content")));

    UpdatePageParams params = new UpdatePageParams();
    params.setTemplate(TemplateParams.templateId(templateSource.getId()));
    params.setEraseContent(true);

    Page updated = getNotionClient().pages().update(target.getId(), params);

    assertEquals(target.getId(), updated.getId());

    BlockList children = getNotionClient().blocks().retrieveChildren(target.getId());
    assertNotNull(children.getResults());
    assertFalse(
        children.getResults().isEmpty(),
        "Target page must contain content copied from the template after the update");
  }

  @Test
  @DisplayName("[IT-39]: Pages - Create page with content and then append more content")
  public void testCreatePageAndAppendContent() {
    Page created =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inPage(pagesTestRootId)
                        .title("[IT-39] Growing page")
                        .children(c -> c.paragraph("Initial paragraph")));

    BlockList beforeAppend = getNotionClient().blocks().retrieveChildren(created.getId());
    assertEquals(1, beforeAppend.getResults().size());

    BlockList afterAppend =
        getNotionClient()
            .blocks()
            .appendChildren(
                created.getId(),
                NotionBlocks.blocksBuilder()
                    .heading2("Added section")
                    .paragraph("Appended paragraph")
                    .bullet("Appended bullet")
                    .build());

    assertEquals(3, afterAppend.getResults().size());

    BlockList all = getNotionClient().blocks().retrieveChildren(created.getId());
    assertEquals(4, all.getResults().size());
    assertEquals("paragraph", all.getResults().get(0).getType());
    assertEquals("heading_2", all.getResults().get(1).getType());
    assertEquals("paragraph", all.getResults().get(2).getType());
    assertEquals("bulleted_list_item", all.getResults().get(3).getType());
  }

  // ===========================================================================
  // 3. Database page properties
  // ===========================================================================

  @Test
  @DisplayName("[IT-63]: Pages - Clear database page properties by setting null values")
  public void testClearDatabasePageProperties() {
    Page created =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inDataSource(DATA_SOURCE_ID)
                        .properties(
                            p ->
                                p.title("Name", "[IT-63] To be cleared")
                                    .richText("Description", "Initial text")
                                    .number("Priority", 3)));

    NumberProperty clearedNumber = new NumberProperty();
    clearedNumber.setNumber(null);
    RichTextProperty clearedText = new RichTextProperty();
    clearedText.setRichText(List.of());

    Page updated =
        getNotionClient()
            .pages()
            .update(
                created.getId(),
                UpdatePageParams.builder()
                    .property("Priority", clearedNumber)
                    .property("Description", clearedText)
                    .build());

    assertNull(updated.getProperties().get("Priority").as(NumberProperty.class).getNumber());
    assertTrue(
        updated
            .getProperties()
            .get("Description")
            .as(RichTextProperty.class)
            .getRichText()
            .isEmpty());
  }

  @Test
  @DisplayName("[IT-64]: Pages - Retrieve a single page property by id")
  public void testRetrieveSingleProperty() {
    Page created =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inDataSource(DATA_SOURCE_ID)
                        .properties(
                            p -> p.title("Name", "[IT-64] Property fetch").number("Priority", 42)));

    String priorityPropertyId = created.getProperties().get("Priority").getId();

    PageProperty property =
        getNotionClient().pages().retrieveProperty(created.getId(), priorityPropertyId);

    assertEquals(42, property.as(NumberProperty.class).getNumber().intValue());
  }

  @Test
  @DisplayName("[IT-65]: Pages - Retrieve a paginated rich_text page property")
  public void testRetrievePaginatedRichTextProperty() {
    Page created =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inDataSource(DATA_SOURCE_ID)
                        .properties(
                            p ->
                                p.title("Name", "[IT-65] Paginated property")
                                    .richText("Description", "Inline rich text content")));

    String descPropertyId = created.getProperties().get("Description").getId();

    PageProperty property =
        getNotionClient().pages().retrieveProperty(created.getId(), descPropertyId);

    // Paginated text properties come back as a NotionList of property_item entries.
    assertNotNull(property.getResults());
    assertFalse(property.getResults().isEmpty());
  }

  @Test
  @DisplayName("[IT-66]: Pages - Update database page properties (number, status, checkbox)")
  public void testUpdateDatabasePageProperties() {
    Page created =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inDataSource(DATA_SOURCE_ID)
                        .properties(
                            p ->
                                p.title("Name", "[IT-66] Update target")
                                    .number("Priority", 1)
                                    .status("Status", "Not started")
                                    .checkbox("Done", false)));

    Page updated =
        getNotionClient()
            .pages()
            .update(
                created.getId(),
                UpdatePageParams.builder()
                    .properties(
                        p ->
                            p.number("Priority", 9).status("Status", "Done").checkbox("Done", true))
                    .build());

    Page retrieved = getNotionClient().pages().retrieve(updated.getId());
    assertEquals(
        9,
        retrieved.getProperties().get("Priority").as(NumberProperty.class).getNumber().intValue());
    assertEquals(
        "Done",
        retrieved.getProperties().get("Status").as(StatusProperty.class).getStatus().getName());
    assertEquals(
        Boolean.TRUE,
        retrieved.getProperties().get("Done").as(CheckboxProperty.class).getCheckbox());
  }
}
