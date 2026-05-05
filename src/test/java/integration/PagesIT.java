package integration;

import static org.junit.jupiter.api.Assertions.*;

import integration.helper.IntegrationTestAssisstant;
import io.kristaxlab.notion.fluent.NotionProperties;
import io.kristaxlab.notion.model.common.FileData;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.UpdatePageParams;
import io.kristaxlab.notion.model.page.property.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for the Pages endpoint with a focus on page properties stored in a Notion data
 * source.
 *
 * <p>All tests in this class use {@link #DATA_SOURCE_ID} as the parent data source. The data source
 * is expected to already exist in the Notion workspace with the schema documented below.
 *
 * <p><b>Required schema for {@code DATA_SOURCE_ID}:</b>
 *
 * <ul>
 *   <li>{@code "Name"} – {@code title}
 *   <li>{@code "Description"} – {@code rich_text}
 *   <li>{@code "Priority"} – {@code number}
 *   <li>{@code "Link"} – {@code url}
 *   <li>{@code "Email"} – {@code email}
 *   <li>{@code "Phone"} – {@code phone_number}
 *   <li>{@code "Done"} – {@code checkbox}
 *   <li>{@code "Category"} – {@code select} with options {@code "Bug"}, {@code "Feature"}, {@code
 *       "Docs"}
 *   <li>{@code "Tags"} – {@code multi_select} with options {@code "urgent"}, {@code "review"},
 *       {@code "low-priority"}
 *   <li>{@code "Status"} – {@code status} with options {@code "Not started"}, {@code "In
 *       progress"}, {@code "Done"}
 *   <li>{@code "Due Date"} – {@code date}
 *   <li>{@code "Owners"} – {@code people}
 *   <li>{@code "Related"} – {@code relation} (self-relation to {@code DATA_SOURCE_ID} is the
 *       simplest setup)
 *   <li>{@code "Attachments"} – {@code files}
 * </ul>
 */
public class PagesIT extends BaseIntegrationTest {

  /** Replace with the id of the prerequisite data source described in the class javadoc. */
  private static final String DATA_SOURCE_ID = "TODO_DATA_SOURCE_ID";

  @Test
  @DisplayName(
      "[IT-70]: Pages - Create with simple text-like properties (title, rich_text,"
          + " number, url, email, phone, checkbox)")
  public void testCreateWithSimpleProperties() {
    // Prerequisite columns: Name (title), Description (rich_text), Priority (number),
    // Link (url), Email (email), Phone (phone_number), Done (checkbox).
    Page created =
        getNotion()
            .pages()
            .create(
                page ->
                    page.underDataSource(DATA_SOURCE_ID)
                        .properties(
                            p ->
                                p.title("Name", "[IT-70] Roadmap")
                                    .richText("Description", "Short description text")
                                    .number("Priority", 5)
                                    .url("Link", "https://example.com")
                                    .email("Email", "test@example.com")
                                    .phoneNumber("Phone", "+1-555-0100")
                                    .checkbox("Done", false)));

    assertNotNull(created);
    assertNotNull(created.getId());

    Page retrieved = getNotion().pages().retrieve(created.getId());
    assertEquals(
        "[IT-70] Roadmap",
        retrieved
            .getProperties()
            .get("Name")
            .as(TitleProperty.class)
            .getTitle()
            .get(0)
            .getPlainText());
    assertEquals(
        "Short description text",
        retrieved
            .getProperties()
            .get("Description")
            .as(RichTextProperty.class)
            .getRichText()
            .get(0)
            .getPlainText());
    assertEquals(
        5,
        retrieved.getProperties().get("Priority").as(NumberProperty.class).getNumber().intValue());
    assertEquals(
        "https://example.com",
        retrieved.getProperties().get("Link").as(UrlProperty.class).getUrl());
    assertEquals(
        "test@example.com",
        retrieved.getProperties().get("Email").as(EmailProperty.class).getEmail());
    assertEquals(
        "+1-555-0100",
        retrieved.getProperties().get("Phone").as(PhoneNumberProperty.class).getPhoneNumber());
    assertEquals(
        Boolean.FALSE,
        retrieved.getProperties().get("Done").as(CheckboxProperty.class).getCheckbox());
  }

  @Test
  @DisplayName("[IT-71]: Pages - Create with select, multi-select and status properties")
  public void testCreateWithSelectProperties() {
    // Prerequisite columns: Name (title), Category (select with "Bug"/"Feature"/"Docs"),
    // Tags (multi_select with "urgent"/"review"/"low-priority"),
    // Status (status with "Not started"/"In progress"/"Done").
    Page created =
        getNotion()
            .pages()
            .create(
                page ->
                    page.underDataSource(DATA_SOURCE_ID)
                        .properties(
                            p ->
                                p.title("Name", "[IT-71] Categorized item")
                                    .select("Category", "Bug")
                                    .multiSelect("Tags", "urgent", "review")
                                    .status("Status", "In progress")));

    Page retrieved = getNotion().pages().retrieve(created.getId());

    assertEquals(
        "Bug",
        retrieved.getProperties().get("Category").as(SelectProperty.class).getSelect().getName());

    List<SelectValue> tags =
        retrieved.getProperties().get("Tags").as(MultiSelectProperty.class).getMultiSelect();
    assertEquals(2, tags.size());
    assertTrue(tags.stream().anyMatch(v -> "urgent".equals(v.getName())));
    assertTrue(tags.stream().anyMatch(v -> "review".equals(v.getName())));

    assertEquals(
        "In progress",
        retrieved.getProperties().get("Status").as(StatusProperty.class).getStatus().getName());
  }

  @Test
  @DisplayName("[IT-72]: Pages - Create with single-day date and date-range properties")
  public void testCreateWithDateProperties() {
    // Prerequisite columns: Name (title), Due Date (date).
    LocalDate due = LocalDate.of(2026, 5, 15);

    Page singleDay =
        getNotion()
            .pages()
            .create(
                page ->
                    page.underDataSource(DATA_SOURCE_ID)
                        .properties(
                            p -> p.title("Name", "[IT-72] Single-day").date("Due Date", due)));

    Page singleDayRetrieved = getNotion().pages().retrieve(singleDay.getId());
    assertEquals(
        due.toString(),
        singleDayRetrieved
            .getProperties()
            .get("Due Date")
            .as(DateProperty.class)
            .getDate()
            .getStart());
    assertNull(
        singleDayRetrieved
            .getProperties()
            .get("Due Date")
            .as(DateProperty.class)
            .getDate()
            .getEnd());

    LocalDateTime start = LocalDateTime.of(2026, 5, 15, 9, 0);
    LocalDateTime end = LocalDateTime.of(2026, 5, 17, 17, 0);

    Page range =
        getNotion()
            .pages()
            .create(
                page ->
                    page.underDataSource(DATA_SOURCE_ID)
                        .properties(
                            p ->
                                p.title("Name", "[IT-72] Range")
                                    .dateRange("Due Date", start, end)));

    Page rangeRetrieved = getNotion().pages().retrieve(range.getId());
    assertNotNull(
        rangeRetrieved.getProperties().get("Due Date").as(DateProperty.class).getDate().getStart());
    assertNotNull(
        rangeRetrieved.getProperties().get("Due Date").as(DateProperty.class).getDate().getEnd());
  }

  @Test
  @DisplayName("[IT-73]: Pages - Create with people, relation, and files properties")
  public void testCreateWithReferenceProperties() {
    // Prerequisite columns: Name (title), Owners (people), Related (relation),
    // Attachments (files).
    // Prerequisites: a person user visible to the integration AND at least one
    // already-existing related page in the same data source (used to create relations).
    String userId = IntegrationTestAssisstant.getPrerequisites().getUserId();

    Page anchor =
        getNotion()
            .pages()
            .create(
                page ->
                    page.underDataSource(DATA_SOURCE_ID)
                        .properties(p -> p.title("Name", "[IT-73] Anchor row")));

    Page created =
        getNotion()
            .pages()
            .create(
                page ->
                    page.underDataSource(DATA_SOURCE_ID)
                        .properties(
                            p ->
                                p.title("Name", "[IT-73] References")
                                    .people("Owners", userId)
                                    .relation("Related", anchor.getId())
                                    .files(
                                        "Attachments",
                                        FileData.builder()
                                            .externalUrl("https://example.com/spec.pdf")
                                            .build())));

    Page retrieved = getNotion().pages().retrieve(created.getId());

    List<io.kristaxlab.notion.model.user.User> owners =
        retrieved.getProperties().get("Owners").as(PeopleProperty.class).getPeople();
    assertEquals(1, owners.size());
    assertEquals(userId, owners.get(0).getId());

    List<RelationProperty.RelationValue> relations =
        retrieved.getProperties().get("Related").as(RelationProperty.class).getRelation();
    assertEquals(1, relations.size());
    // Notion normalizes ids; compare hex digits only.
    assertEquals(anchor.getId().replace("-", ""), relations.get(0).getId().replace("-", ""));

    List<FileData> files =
        retrieved.getProperties().get("Attachments").as(FilesProperty.class).getFiles();
    assertEquals(1, files.size());
    assertNotNull(files.get(0).getExternal());
  }

  @Test
  @DisplayName("[IT-74]: Pages - Create page with properties and content blocks combined")
  public void testCreateWithPropertiesAndChildren() {
    // Prerequisite columns: Name (title), Description (rich_text). The page is also given a
    // body composed with the blocks DSL.
    Page created =
        getNotion()
            .pages()
            .create(
                page ->
                    page.underDataSource(DATA_SOURCE_ID)
                        .properties(
                            p ->
                                p.title("Name", "[IT-74] With body")
                                    .richText("Description", "Has both properties and children"))
                        .icon("📚")
                        .children(
                            c ->
                                c.heading2("Section")
                                    .paragraph("Body paragraph")
                                    .todo("First task")
                                    .todo("Second task")));

    assertNotNull(created.getId());
    assertNotNull(created.getIcon());

    var children = getNotion().blocks().retrieveChildren(created.getId()).getResults();
    assertEquals(4, children.size());
    assertEquals("heading_2", children.get(0).getType());
    assertEquals("paragraph", children.get(1).getType());
    assertEquals("to_do", children.get(2).getType());
  }

  @Test
  @DisplayName(
      "[IT-75]: Pages - Create page using NotionProperties static factories via property() escape"
          + " hatch")
  public void testCreateWithStaticFactories() {
    // Prerequisite columns: Name (title), Priority (number), Status (status with "Done").
    // This test verifies that NotionProperties static factories can be combined with the
    // .property(name, value) escape hatch — useful when properties are built elsewhere.
    Page created =
        getNotion()
            .pages()
            .create(
                page ->
                    page.underDataSource(DATA_SOURCE_ID)
                        .property("Name", NotionProperties.title("[IT-75] Static factories"))
                        .property("Priority", NotionProperties.number(7))
                        .property("Status", NotionProperties.status("Done")));

    Page retrieved = getNotion().pages().retrieve(created.getId());
    assertEquals(
        7,
        retrieved.getProperties().get("Priority").as(NumberProperty.class).getNumber().intValue());
    assertEquals(
        "Done",
        retrieved.getProperties().get("Status").as(StatusProperty.class).getStatus().getName());
  }

  // -------------------------------------------------------------------------
  // Update / clear
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("[IT-76]: Pages - Update properties (number, status, checkbox)")
  public void testUpdateProperties() {
    // Prerequisite columns: Name (title), Priority (number), Status (status with "Not
    // started" and "Done"), Done (checkbox).
    Page created =
        getNotion()
            .pages()
            .create(
                page ->
                    page.underDataSource(DATA_SOURCE_ID)
                        .properties(
                            p ->
                                p.title("Name", "[IT-76] Update target")
                                    .number("Priority", 1)
                                    .status("Status", "Not started")
                                    .checkbox("Done", false)));

    Page updated =
        getNotion()
            .pages()
            .update(
                created.getId(),
                UpdatePageParams.builder()
                    .properties(
                        p ->
                            p.number("Priority", 9).status("Status", "Done").checkbox("Done", true))
                    .build());

    assertEquals(
        9, updated.getProperties().get("Priority").as(NumberProperty.class).getNumber().intValue());
    assertEquals(
        "Done",
        updated.getProperties().get("Status").as(StatusProperty.class).getStatus().getName());
    assertEquals(
        Boolean.TRUE, updated.getProperties().get("Done").as(CheckboxProperty.class).getCheckbox());
  }

  @Test
  @DisplayName("[IT-77]: Pages - Clear properties by setting null values")
  public void testClearProperties() {
    // Prerequisite columns: Name (title), Description (rich_text), Priority (number).
    // Tests that properties created with non-null values can be cleared by sending properties
    // with null payloads via the property() escape hatch.
    Page created =
        getNotion()
            .pages()
            .create(
                page ->
                    page.underDataSource(DATA_SOURCE_ID)
                        .properties(
                            p ->
                                p.title("Name", "[IT-77] To be cleared")
                                    .richText("Description", "Initial text")
                                    .number("Priority", 3)));

    NumberProperty clearedNumber = new NumberProperty();
    clearedNumber.setNumber(null);
    RichTextProperty clearedText = new RichTextProperty();
    clearedText.setRichText(List.of());

    Page updated =
        getNotion()
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

  // -------------------------------------------------------------------------
  // Retrieve single property
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("[IT-78]: Pages - Retrieve a single property by id")
  public void testRetrieveSingleProperty() {
    // Prerequisite columns: Name (title), Priority (number). Tests the
    // /pages/{page_id}/properties/{property_id} endpoint for a non-paginated
    // property type (number).
    Page created =
        getNotion()
            .pages()
            .create(
                page ->
                    page.underDataSource(DATA_SOURCE_ID)
                        .properties(
                            p -> p.title("Name", "[IT-78] Property fetch").number("Priority", 42)));

    String priorityPropertyId = created.getProperties().get("Priority").getId();

    PageProperty property =
        getNotion().pages().retrieveProperty(created.getId(), priorityPropertyId);

    assertEquals(42, property.as(NumberProperty.class).getNumber().intValue());
  }

  @Test
  @DisplayName("[IT-79]: Pages - Retrieve a paginated rich_text property")
  public void testRetrievePaginatedRichTextProperty() {
    // Prerequisite columns: Name (title), Description (rich_text). The rich_text property
    // type is exposed as a paginated list of property_item objects when fetched via
    // /pages/{page_id}/properties/{property_id}.
    Page created =
        getNotion()
            .pages()
            .create(
                page ->
                    page.underDataSource(DATA_SOURCE_ID)
                        .properties(
                            p ->
                                p.title("Name", "[IT-79] Paginated property")
                                    .richText("Description", "Inline rich text content")));

    String descPropertyId = created.getProperties().get("Description").getId();

    PageProperty property = getNotion().pages().retrieveProperty(created.getId(), descPropertyId);

    // Paginated text properties come back as a NotionList of property_item entries.
    assertNotNull(property.getResults());
    assertFalse(property.getResults().isEmpty());
  }
}
