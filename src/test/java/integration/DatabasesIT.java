package integration;

import static org.junit.jupiter.api.Assertions.*;

import integration.util.IntegrationTestAssisstant;
import io.kristixlab.notion.api.model.common.CoverParams;
import io.kristixlab.notion.api.model.common.IconParams;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.common.RichText;
import io.kristixlab.notion.api.model.databases.*;
import io.kristixlab.notion.api.model.datasources.CreateDataSourceParams;
import io.kristixlab.notion.api.model.datasources.DataSource;
import io.kristixlab.notion.api.model.datasources.UpdateDataSourceParams;
import io.kristixlab.notion.api.model.datasources.properties.NumberSchemaParams;
import io.kristixlab.notion.api.model.datasources.properties.TitleSchemaParams;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.*;

public class DatabasesIT extends BaseIntegrationTest {
  private static String databasesTestsPageId;
  private String currTestPageId;

  @BeforeAll
  public static void setup() {
    databasesTestsPageId = IntegrationTestAssisstant.createPageForTests("Databases");
  }

  @BeforeEach
  public void beforeEachTest(TestInfo info) {
    super.beforeEach(info);
    currTestPageId =
        IntegrationTestAssisstant.createPageForTests(info.getDisplayName(), databasesTestsPageId);
  }

  @Test
  @DisplayName("[IT-33]: Databases - Create a new database with icon and cover")
  public void createDatabaseWithIconAndCover() {
    String coverUrl = IntegrationTestAssisstant.getPrerequisites().getExternalImageUrl();
    String emoji = IntegrationTestAssisstant.getPrerequisites().getEmojiIcon();

    CreateDatabaseParams params = new CreateDatabaseParams();
    params.setParent(Parent.pageParent(currTestPageId));
    params.setTitle(toRichTextList("Database with icon and cover"));
    params.setIcon(IconParams.fromEmoji(emoji));
    params.setCover(CoverParams.fromExternalUrl(coverUrl));

    Database database = getNotion().databases().create(params);
    assertNotNull(database.getId());
    assertNotNull(database.getIcon());
    assertNotNull(database.getCover());

    Database retrieved = getNotion().databases().retrieve(database.getId());
    assertNotNull(retrieved);
    assertEquals(database.getId(), retrieved.getId());
  }

  @Test
  @DisplayName(
      "[IT-46]: Databases - Create a database, then update its title, icon, cover and set parent to another page")
  public void createAndUpdateDatabase() {
    // Step 1: Create a database with an initial title
    CreateDatabaseParams createParams = new CreateDatabaseParams();
    createParams.setParent(Parent.pageParent(currTestPageId));
    createParams.setTitle(toRichTextList("Database to be updated"));
    Database database = getNotion().databases().create(createParams);
    assertNotNull(database.getId());

    // Step 2: Create a separate page to serve as the new parent
    String newParentPageId =
        IntegrationTestAssisstant.createPageForTests("New parent page", currTestPageId);

    // Step 3: Update title, icon, cover and parent
    String coverUrl = IntegrationTestAssisstant.getPrerequisites().getExternalImageUrl();
    String emoji = IntegrationTestAssisstant.getPrerequisites().getEmojiIcon();

    UpdateDatabaseParams updateParams = new UpdateDatabaseParams();
    updateParams.setTitle(toRichTextList("Updated database title"));
    updateParams.setIcon(IconParams.fromEmoji(emoji));
    updateParams.setCover(CoverParams.fromExternalUrl(coverUrl));
    updateParams.setParent(Parent.pageParent(newParentPageId));

    Database updated = getNotion().databases().update(database.getId(), updateParams);

    assertNotNull(updated.getTitle());
    assertNotNull(updated.getParent());
    assertEquals(newParentPageId, updated.getParent().getPageId());
    assertNotNull(updated.getIcon());
    assertNotNull(updated.getCover());
  }

  @Test
  @DisplayName(
      "[IT-47]: Databases & Data Sources - Create a database, then add a second data source to it")
  public void createDatabaseAndAddSecondDataSource() {
    // Step 1: Create a database (gets one data source by default)
    CreateDatabaseParams createParams = new CreateDatabaseParams();
    createParams.setParent(Parent.pageParent(currTestPageId));
    createParams.setTitle(toRichTextList("Database with multiple data sources"));
    createParams.setIsInline(true);
    Database database = getNotion().databases().create(createParams);

    assertNotNull(database.getId());
    assertNotNull(database.getDataSources());
    assertEquals(1, database.getDataSources().size());

    // Step 2: Create a second data source linked to the same database
    CreateDataSourceParams dsRequest = new CreateDataSourceParams();
    dsRequest.setProperties(new HashMap<>());
    dsRequest.getProperties().put("Name", new TitleSchemaParams());
    dsRequest.getProperties().put("Number", new NumberSchemaParams());
    dsRequest.setParent(Parent.databaseParent(database.getId()));
    dsRequest.setTitle(toRichTextList("Second data source"));

    DataSource secondDataSource = getNotion().dataSources().create(dsRequest);
    assertNotNull(secondDataSource.getId());

    // Step 3: Retrieve the database and verify it now has 2 data sources
    Database retrieved = getNotion().databases().retrieve(database.getId());
    assertNotNull(retrieved.getDataSources());
    assertEquals(2, retrieved.getDataSources().size());
  }

  @Test
  @DisplayName("[IT-48]: Databases - Create an inline database, then change it to be not inline.")
  public void createInlineDatabaseThenChangeInlineLockUnlock() {
    // Step 1: Create an inline database
    CreateDatabaseParams createParams = new CreateDatabaseParams();
    createParams.setParent(Parent.pageParent(currTestPageId));
    createParams.setTitle(toRichTextList("Inline database"));
    createParams.setIsInline(true);
    Database database = getNotion().databases().create(createParams);
    assertNotNull(database.getId());
    assertTrue(database.getIsInline());

    String id = database.getId();

    // Step 2: Change to non-inline
    UpdateDatabaseParams toNotInline = new UpdateDatabaseParams();
    toNotInline.setIsInline(false);
    Database notInline = getNotion().databases().update(id, toNotInline);
    assertFalse(notInline.getIsInline());
  }

  @Test
  @DisplayName("[IT-49]: Databases - Create a database. Then lock it, then unlock it.")
  public void createDatabaseThenLockAndUnlock() {
    // Step 1: Create a database
    CreateDatabaseParams createParams = new CreateDatabaseParams();
    createParams.setParent(Parent.pageParent(currTestPageId));
    createParams.setTitle(toRichTextList("Database to lock and unlock"));
    Database database = getNotion().databases().create(createParams);
    assertNotNull(database);

    String id = database.getId();

    // Step 2: Lock the database
    UpdateDatabaseParams toLocked = new UpdateDatabaseParams();
    toLocked.setIsLocked(true);
    Database locked = getNotion().databases().update(id, toLocked);
    assertTrue(locked.getIsLocked());

    // Step 3: Unlock the database
    UpdateDatabaseParams toUnlocked = new UpdateDatabaseParams();
    toUnlocked.setIsLocked(false);
    Database unlocked = getNotion().databases().update(id, toUnlocked);
    assertFalse(unlocked.getIsLocked());
  }

  @Test
  @DisplayName("[IT-50]: Databases - Create a database. Then remove it, then restore it.")
  public void createDatabaseThenDeleteAndRestore() {
    // Step 1: Create a database
    CreateDatabaseParams createParams = new CreateDatabaseParams();
    createParams.setParent(Parent.pageParent(currTestPageId));
    createParams.setTitle(toRichTextList("Database to delete and restore"));
    Database database = getNotion().databases().create(createParams);
    assertNotNull(database.getId());

    String id = database.getId();

    // Step 2: Delete (move to trash)
    Database deleted = getNotion().databases().delete(id);
    assertTrue(deleted.getInTrash());

    // Step 3: Restore
    Database restored = getNotion().databases().restore(id);
    assertFalse(restored.getInTrash());
  }

  @Test
  @DisplayName(
      "[IT-51]: Data sources,Databases - Create a database with 2 datasources, then move those data sources to another database")
  public void createDatabaseWith2DataSourcesThenMoveToAnotherDatabase() {
    // Step 1: Create the source database (gets one data source by default)
    CreateDatabaseParams sourceParams = new CreateDatabaseParams();
    sourceParams.setParent(Parent.pageParent(currTestPageId));
    sourceParams.setTitle(toRichTextList("Source database"));
    sourceParams.setIsInline(true);
    sourceParams.setInitialDataSource(new InitialDatasource());
    sourceParams.getInitialDataSource().setProperties(new HashMap<>());
    sourceParams
        .getInitialDataSource()
        .getProperties()
        .put("Title property", new TitleSchemaParams());
    Database sourceDb = getNotion().databases().create(sourceParams);
    assertEquals(1, sourceDb.getDataSources().size());

    // Step 2: Add a second data source to the source database
    CreateDataSourceParams dsRequest =
        createDataSourceParams("Second data source", sourceDb.getId());
    DataSource secondDs = getNotion().dataSources().create(dsRequest);

    Database sourceWithTwo = getNotion().databases().retrieve(sourceDb.getId());
    assertEquals(2, sourceWithTwo.getDataSources().size());

    // Step 3: Create the target database
    CreateDatabaseParams targetParams = new CreateDatabaseParams();
    targetParams.setParent(Parent.pageParent(currTestPageId));
    targetParams.setTitle(toRichTextList("Target database"));
    targetParams.setIsInline(true);
    Database targetDb = getNotion().databases().create(targetParams);
    int targetInitialCount = targetDb.getDataSources().size();

    // Step 4: Move both data sources from source to target
    UpdateDataSourceParams moveRequest = new UpdateDataSourceParams();
    moveRequest.setParent(Parent.databaseParent(targetDb.getId()));
    for (DataSourceRef dsRef : sourceWithTwo.getDataSources()) {
      getNotion().dataSources().update(dsRef.getId(), moveRequest);
    }

    // Step 5: Verify source has 0 data sources, target gained both
    Database sourceAfter = getNotion().databases().retrieve(sourceDb.getId());
    assertEquals(0, sourceAfter.getDataSources().size());

    Database targetAfter = getNotion().databases().retrieve(targetDb.getId());
    assertEquals(targetInitialCount + 2, targetAfter.getDataSources().size());
  }

  private List<RichText> toRichTextList(String text) {
    RichText richText = RichText.builder().fromText(text).build();
    return List.of(richText);
  }

  private CreateDataSourceParams createDataSourceParams(String title, String parentDatabaseId) {
    CreateDataSourceParams params = new CreateDataSourceParams();
    params.setTitle(toRichTextList(title));
    params.setProperties(new HashMap<>());
    params.getProperties().put("Name", new TitleSchemaParams());
    params.setParent(Parent.databaseParent(parentDatabaseId));
    return params;
  }
}
