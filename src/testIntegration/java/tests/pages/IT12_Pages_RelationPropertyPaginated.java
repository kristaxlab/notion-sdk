package tests.pages;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.model.database.CreateDatabaseParams;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.property.ListedRelation;
import io.kristaxlab.notion.model.page.property.RelationPropertyList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

@Tag("heavy")
public class IT12_Pages_RelationPropertyPaginated extends WithEmptyTestPage {

  private static final String TITLE_PROP = "Name";
  private static final String RELATION_PROP = "Related";
  private static final int CHILD_COUNT = 35;
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

    getSetupClient()
        .dataSources()
        .update(dataSourceId, ds -> ds.properties(p -> p.relation(RELATION_PROP, dataSourceId)));
  }

  @Test
  @DisplayName("IT-12: Pages - Paginated property retrieve for relation")
  public void testPaginatedPropertyRetrieve() {
    List<String> childIds = new ArrayList<>(CHILD_COUNT);
    for (int i = 0; i < CHILD_COUNT; i++) {
      childIds.add(createChild("Child " + i));
    }

    Page newPage =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inDataSource(dataSourceId)
                        .properties(
                            p -> p.title(TITLE_PROP, "Parent").relation(RELATION_PROP, childIds)));

    String relationPropertyId = newPage.getProperties().get(RELATION_PROP).getId();
    assertNotNull(relationPropertyId);

    Set<String> retrieved = new HashSet<>();
    String cursor = null;
    int pageCount = 0;

    do {
      RelationPropertyList propertyList =
          getNotionClient()
              .pages()
              .retrievePaginatedProperty(newPage.getId(), relationPropertyId, cursor, PAGE_SIZE)
              .asRelationList();

      assertNotNull(propertyList.getPropertyItem());
      assertEquals("relation", propertyList.getPropertyItem().getType());
      assertNotNull(propertyList.getResults());
      assertFalse(propertyList.getResults().isEmpty());
      assertTrue(propertyList.getResults().size() <= PAGE_SIZE);

      for (ListedRelation item : propertyList.getResults()) {
        assertNotNull(item.getRelation());
        retrieved.add(item.getRelation().getId());
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

    assertTrue(pageCount > 1, "Expected multiple pages for " + CHILD_COUNT + " relations");
    assertEquals(CHILD_COUNT, retrieved.size());
    assertTrue(retrieved.containsAll(childIds));
  }

  private String createChild(String title) {
    return getNotionClient()
        .pages()
        .create(page -> page.inDataSource(dataSourceId).properties(p -> p.title(TITLE_PROP, title)))
        .getId();
  }
}
