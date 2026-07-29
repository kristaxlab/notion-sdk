package integration.pages;

import integration.BaseIntegrationTest;
import integration.extension.NotionTestPage;
import io.kristaxlab.notion.fluent.NotionProperties;
import io.kristaxlab.notion.model.database.CreateDatabaseParams;
import io.kristaxlab.notion.model.page.property.PageProperty;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PagesIT7_PropertyReadPaginated extends BaseIntegrationTest {

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
                    .properties(p -> p.richText("Notes"))
                    .build())
            .getDataSources()
            .get(0)
            .getId();
  }

  @Test
  @DisplayName("IT-?: Pages - Full CRUD cycle for all supported property values")
  public void testPropertyCrud() {
    // Phase 1: CREATE with initial values
    Map<String, PageProperty> phase1Props =
        NotionProperties.builder()
            .title("Name", "Initial Title")
            .richText("Notes", "Initial notes")
            .build();
  }
}
