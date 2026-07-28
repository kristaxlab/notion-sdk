package integration.pages;

import integration.BaseIntegrationTest;
import integration.NotionTstPageLogExtension;
import integration.extension.NotionTestPage;
import io.kristaxlab.notion.http.error.ValidationException;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.common.Parent;
import io.kristaxlab.notion.model.database.CreateDatabaseParams;
import io.kristaxlab.notion.model.database.Database;
import io.kristaxlab.notion.model.page.Page;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

@DisplayName("IT-4: Pages - Move page to other parents")
public class Pages_IT4_Move extends BaseIntegrationTest {

  @NotionTestPage
  private static String testPageId;
  
  private static String databaseId;
  private static String dataSourceId;
  private static String blockId;

  @BeforeEach
  public void setup() {
    NotionTstPageLogExtension.register(Pages_IT4_Move.class, testPageId);

    Database db = createDatabase(testPageId);
    databaseId = db.getId();
    dataSourceId = db.getDataSources().get(0).getId();
    blockId = createBlock(testPageId);
  }

  private static String createBlock(String parentPageId) {
    BlockList blockList =
            getSetupClient().blocks().appendChildren(parentPageId, b -> b.toggle("Toggle block"));
    if (blockList == null || blockList.getResults().isEmpty()) {
      throw new IllegalStateException("Failed to create toggle block");
    }
    return blockList.getResults().get(0).getId();
  }

  private static Database createDatabase(String parentPageId) {
    Database db =
            getSetupClient()
                    .databases()
                    .create(
                            CreateDatabaseParams.builder()
                                    .inPage(parentPageId)
                                    .title("IT-7 Test Database")
                                    .build());
    if (db.getDataSources() == null || db.getDataSources().size() != 1) {
      throw new IllegalStateException("A testing database is supposed to have 1 data source");
    }
    return db;
  }

  @Test
  @DisplayName("IT-4: Pages - Move page to other parents")
  public void testDataSourceSchemaCrudOperations() {

    // Create an anchor row in the related data source so we can demonstrate the relation properties
    Page page = getNotionClient().pages().create(p -> p.inPage(testPageId));

    assertNotNull(page.getParent());
    assertEquals("page_id", page.getParent().getType());
    assertEquals(testPageId, page.getParent().getPageId());

    Page movedToDataSource =
            getNotionClient().pages().move(page.getId(), Parent.dataSourceParent(dataSourceId));

    assertNotNull(movedToDataSource.getParent());
    assertEquals("data_source_id", movedToDataSource.getParent().getType());
    assertEquals(dataSourceId, movedToDataSource.getParent().getDataSourceId());

    Page movedBack = getNotionClient().pages().move(page.getId(), Parent.pageParent(testPageId));

    assertNotNull(movedBack.getParent());
    assertEquals("page_id", movedBack.getParent().getType());
    assertEquals(testPageId, movedBack.getParent().getPageId());

    assertThrows(
            "Moving a page to a block parent should throw ValidationException",
            ValidationException.class,
            () -> getNotionClient().pages().move(page.getId(), Parent.blockParent(blockId)));

    assertThrows(
            "Moving a page to a database should throw ValidationException",
            ValidationException.class,
            () -> getNotionClient().pages().move(page.getId(), Parent.databaseParent(databaseId)));

    assertThrows(
            "Moving a page to workspace root should throw ValidationException",
            ValidationException.class,
            () -> getNotionClient().pages().move(page.getId(), Parent.workspaceParent()));
  }

  @AfterAll
  public static void tearDown() {
  }
}
