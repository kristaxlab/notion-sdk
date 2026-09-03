package tests.pages;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.fluent.NotionProperties;
import io.kristaxlab.notion.model.database.CreateDatabaseParams;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.property.PagePropertyValue;
import io.kristaxlab.notion.model.page.property.PeopleProperty;
import io.kristaxlab.notion.model.page.property.PeoplePropertyList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;
import testkit.ext.TestSession;

public class IT13_Pages_PeopleProperty extends WithEmptyTestPage {

  private static final String PEOPLE_PROP = "People";
  private String dataSourceId;
  private static String userId;

  @BeforeEach
  public void setup() {
    userId = TestSession.get().getBotUserId();
    dataSourceId =
        getSetupClient()
            .databases()
            .create(
                CreateDatabaseParams.builder()
                    .inPage(getTestPageId())
                    .title("Test Database")
                    .isInline(true)
                    .properties(p -> p.people(PEOPLE_PROP))
                    .build())
            .getDataSources()
            .get(0)
            .getId();
  }

  @Test
  @DisplayName("IT-?: Pages - Create and retrieve 'people' property")
  public void testPaginatedPropertyRetrieve() {

    Page newPage =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inDataSource(dataSourceId)
                        .property(PEOPLE_PROP, NotionProperties.people(userId)));

    PagePropertyValue peopleProperty = newPage.getProperties().get(PEOPLE_PROP);

    assertNotNull(peopleProperty);
    assertInstanceOf(PeopleProperty.class, peopleProperty);
    assertEquals("people", peopleProperty.getType());
    assertEquals(1, peopleProperty.as(PeopleProperty.class).getPeople().size());
    assertEquals(userId, peopleProperty.as(PeopleProperty.class).getPeople().get(0).getId());

    PeoplePropertyList standaloneProperty =
        getNotionClient()
            .pages()
            .retrievePaginatedProperty(newPage.getId(), peopleProperty.getId())
            .asPeopleList();

    assertNotNull(standaloneProperty.getPropertyItem());
    assertEquals("people", standaloneProperty.getPropertyItem().getType());
    assertNotNull(standaloneProperty.getResults());
    assertFalse(standaloneProperty.getResults().isEmpty());
    assertEquals(1, standaloneProperty.getResults().size());
    assertEquals(userId, standaloneProperty.getResults().get(0).getPeople().getId());
  }
}
