package integration.pages;

import static org.junit.jupiter.api.Assertions.*;

import integration.BaseIntegrationTest;
import integration.NotionTstPageLogExtension;
import integration.extension.NotionTestPage;
import io.kristaxlab.notion.fluent.NotionProperties;
import io.kristaxlab.notion.model.database.CreateDatabaseParams;
import io.kristaxlab.notion.model.page.property.PageProperty;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("IT-XXX: Pages - Full CRUD cycle for all supported property values")
public class Pages_IT7_PropertyReadPaginated extends BaseIntegrationTest {

  @NotionTestPage private static String testPageId;

  private String dataSourceId;

  @BeforeEach
  public void setup() {
    NotionTstPageLogExtension.register(Pages_IT7_PropertyReadPaginated.class, testPageId);

    dataSourceId =
        getSetupClient()
            .databases()
            .create(
                CreateDatabaseParams.builder()
                    .inPage(testPageId)
                    .title("Test Database")
                    .properties(p -> p.richText("Notes"))
                    .build())
            .getDataSources()
            .get(0)
            .getId();
  }

  @Test
  public void testPropertyCrud() {
    // Phase 1: CREATE with initial values
    Map<String, PageProperty> phase1Props =
        NotionProperties.builder()
            .title("Name", "Initial Title")
            .richText("Notes", "Initial notes")
            .build();
  }
}
