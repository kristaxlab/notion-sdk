package integration;

import static org.junit.jupiter.api.Assertions.*;

import integration.util.IntegrationTestAssisstant;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.common.RichText;
import io.kristixlab.notion.api.model.databases.CreateDatabaseParams;
import io.kristixlab.notion.api.model.databases.Database;
import io.kristixlab.notion.api.model.datasources.CreateDataSourceParams;
import io.kristixlab.notion.api.model.datasources.DataSource;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.*;

public class DataSourcesIT extends BaseIntegrationTest {
  private static String dataSourceTestsPageId;
  private String currTestPageId;

  @BeforeAll
  public static void setup() {
    dataSourceTestsPageId = IntegrationTestAssisstant.createPageForTests("Data Sources");
  }

  @BeforeEach
  public void beforeEachTest(TestInfo info) {
    super.beforeEach(info);
    currTestPageId =
        IntegrationTestAssisstant.createPageForTests(info.getDisplayName(), dataSourceTestsPageId);
  }


  private List<RichText> toRichTextList(String text) {
    RichText richText = RichText.builder().fromText(text).build();
    return List.of(richText);
  }
}
