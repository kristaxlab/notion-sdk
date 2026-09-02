package tests.pages;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.fluent.NotionProperties;
import io.kristaxlab.notion.fluent.NotionText;
import io.kristaxlab.notion.http.error.ValidationException;
import io.kristaxlab.notion.model.common.richtext.RichText;
import io.kristaxlab.notion.model.database.CreateDatabaseParams;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.property.ListedRichText;
import io.kristaxlab.notion.model.page.property.RichTextPropertyList;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

public class IT7_Pages_RichTextPropertyPaginated extends WithEmptyTestPage {

  private static final String NOTES_PROP = "Notes";
  private static final String RICH_TEXT_CONTENT = "Simple text ==";
  private static final int RICH_TEXT_COUNT = 100;
  private static final int PAGE_SIZE = 30;

  private String dataSourceId;

  @BeforeEach
  public void setup() {
    dataSourceId =
        getSetupClient()
            .databases()
            .create(
                CreateDatabaseParams.builder()
                    .inPage(getTestPageId())
                    .title("Test Database")
                    .properties(p -> p.richText(NOTES_PROP).checkbox("Done"))
                    .build())
            .getDataSources()
            .get(0)
            .getId();
  }

  @Test
  @DisplayName("IT-7: Pages - Paginated property retrieve for rich text")
  public void testPaginatedPropertyRetrieve() {

    // TODO move to a limits test
    assertThrows(
        ValidationException.class,
        () ->
            getNotionClient()
                .pages()
                .create(
                    page ->
                        page.inDataSource(dataSourceId)
                            .properties(
                                prop ->
                                    prop.richText(NOTES_PROP, createRichTexts("text", 150))
                                        .checkbox("Done", true))));

    Page newPage =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inDataSource(dataSourceId)
                        .property(
                            NOTES_PROP,
                            NotionProperties.richText(
                                createRichTexts(RICH_TEXT_CONTENT, RICH_TEXT_COUNT))));

    String notesPropertyId = newPage.getProperties().get(NOTES_PROP).getId();
    assertNotNull(notesPropertyId);

    List<RichText> retrieved = new ArrayList<>();
    String cursor = null;
    int pageCount = 0;

    do {
      RichTextPropertyList propertyList =
          getNotionClient()
              .pages()
              .retrievePaginatedProperty(newPage.getId(), notesPropertyId, cursor, PAGE_SIZE)
              .asRichTextList();

      assertNotNull(propertyList.getPropertyItem());
      assertEquals("rich_text", propertyList.getPropertyItem().getType());
      assertNotNull(propertyList.getResults());
      assertFalse(propertyList.getResults().isEmpty());
      assertTrue(propertyList.getResults().size() <= PAGE_SIZE);

      for (ListedRichText item : propertyList.getResults()) {
        assertNotNull(item.getRichText());
        retrieved.add(item.getRichText());
      }

      pageCount++;
      if (Boolean.TRUE.equals(propertyList.getHasMore())) {
        assertNotNull(propertyList.getNextCursor());
        cursor = propertyList.getNextCursor();
      } else {
        assertNull(propertyList.getNextCursor());
        cursor = null;
      }
    } while (cursor != null);

    assertTrue(pageCount > 1, "Expected multiple pages for " + RICH_TEXT_COUNT + " items");
    assertEquals(RICH_TEXT_COUNT, retrieved.size());
    for (RichText richText : retrieved) {
      assertEquals(RICH_TEXT_CONTENT, richText.getPlainText());
    }
  }

  private List<RichText> createRichTexts(String text, int times) {
    List<RichText> richTexts = new ArrayList<>();
    for (int i = 0; i < times; i++) {
      richTexts.add(NotionText.plainText(text));
    }
    return richTexts;
  }
}
