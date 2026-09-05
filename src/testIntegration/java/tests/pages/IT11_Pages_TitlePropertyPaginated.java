package tests.pages;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.fluent.NotionProperties;
import io.kristaxlab.notion.fluent.NotionText;
import io.kristaxlab.notion.model.common.richtext.RichText;
import io.kristaxlab.notion.model.database.CreateDatabaseParams;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.property.ListedRichText;
import io.kristaxlab.notion.model.page.property.TitlePropertyList;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

public class IT11_Pages_TitlePropertyPaginated extends WithEmptyTestPage {

  private static final String TITLE_PROP = "Name";
  private static final int TITLE_COUNT = 100;
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
                    .isInline(true)
                    .properties(p -> p.title(TITLE_PROP))
                    .build())
            .getDataSources()
            .get(0)
            .getId();
  }

  @Test
  @DisplayName("IT-11: Pages - Paginated property retrieve for title")
  public void testPaginatedPropertyRetrieve() {
    Page newPage =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inDataSource(dataSourceId)
                        .property(
                            TITLE_PROP, NotionProperties.title(createTitleRuns(TITLE_COUNT))));

    String titlePropertyId = newPage.getProperties().get(TITLE_PROP).getId();
    assertNotNull(titlePropertyId);

    List<RichText> retrieved = new ArrayList<>();
    String cursor = null;
    int pageCount = 0;

    do {
      TitlePropertyList propertyList =
          getNotionClient()
              .pages()
              .retrievePaginatedProperty(newPage.getId(), titlePropertyId, cursor, PAGE_SIZE)
              .asTitleList();

      assertNotNull(propertyList.getPropertyItem());
      assertEquals("title", propertyList.getPropertyItem().getType());
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

    assertTrue(pageCount > 1, "Expected multiple pages for " + TITLE_COUNT + " items");
    assertEquals(TITLE_COUNT, retrieved.size());
    for (int i = 0; i < retrieved.size(); i++) {
      assertEquals(titleRunText(i), retrieved.get(i).getPlainText());
    }
  }

  private List<RichText> createTitleRuns(int times) {
    List<RichText> richTexts = new ArrayList<>();
    for (int i = 0; i < times; i++) {
      richTexts.add(titleRun(i));
    }
    return richTexts;
  }

  private static RichText titleRun(int index) {
    RichText richText = NotionText.plainText(titleRunText(index));
    richText.getAnnotations().setBold(index % 2 == 1);
    return richText;
  }

  private static String titleRunText(int index) {
    return "T" + index;
  }
}
