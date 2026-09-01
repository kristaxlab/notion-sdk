package tests.pages;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.fluent.NotionProperties;
import io.kristaxlab.notion.fluent.NotionText;
import io.kristaxlab.notion.http.error.ValidationException;
import io.kristaxlab.notion.model.common.richtext.RichText;
import io.kristaxlab.notion.model.database.CreateDatabaseParams;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.property.PageProperty;
import io.kristaxlab.notion.model.page.property.PropertyItem;
import io.kristaxlab.notion.model.page.property.list.ListedPageProperty;
import io.kristaxlab.notion.model.page.property.list.ListedRichTextProperty;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

public class IT7_Pages_PropertyStandalone extends WithEmptyTestPage {

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
    boolean sawPartialPage = false;

    do {
      PageProperty prop =
          getNotionClient()
              .pages()
              .retrieveProperty(newPage.getId(), notesPropertyId, cursor, PAGE_SIZE);

      assertInstanceOf(PropertyItem.class, prop);
      PropertyItem propertyItem = prop.as(PropertyItem.class);
      assertEquals("property_item", propertyItem.getType());
      assertNotNull(propertyItem.getPropertyItem());
      assertEquals("rich_text", propertyItem.getPropertyItem().getType());
      assertNotNull(propertyItem.getResults());
      assertFalse(propertyItem.getResults().isEmpty());
      assertTrue(propertyItem.getResults().size() <= PAGE_SIZE);

      for (ListedPageProperty listed : propertyItem.getResults()) {
        assertInstanceOf(ListedRichTextProperty.class, listed);
        ListedRichTextProperty richTextItem = listed.asRichText();
        assertNotNull(richTextItem.getRichText());
        retrieved.add(richTextItem.getRichText());
      }

      pageCount++;
      if (Boolean.TRUE.equals(propertyItem.getHasMore())) {
        assertNotNull(propertyItem.getNextCursor());
        cursor = propertyItem.getNextCursor();
      } else {
        assertNull(propertyItem.getNextCursor());
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
