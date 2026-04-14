package integration.helper;

import static org.junit.jupiter.api.Assertions.fail;

import io.kristaxlab.notion.NotionClient;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.common.Parent;
import io.kristaxlab.notion.fluent.NotionProperties;
import io.kristaxlab.notion.model.page.CreatePageParams;
import io.kristaxlab.notion.model.page.property.TitleProperty;
import java.io.File;
import java.net.URL;
import java.util.HashMap;

public class IntegrationTestAssisstant {

  private static IntegrationTestAssisstant INSTANCE;
  private static Prerequisites prerequisites;
  private static String testPageId;

  public static NotionClient getNotion() {
    return NotionTestClientProvider.internalTestingClient();
  }

  private static IntegrationTestAssisstant getInstance() {
    if (INSTANCE == null) {
      createInstance();
    }
    return INSTANCE;
  }

  private static synchronized void createInstance() {
    INSTANCE = new IntegrationTestAssisstant();

    BlockList rootPageBlocks = getNotion().blocks().retrieveChildren(getRootPageId());
    loadPrerequisites(findPrerequisitesPageId(rootPageBlocks));
    createTestPage(findTestSessionsDatabase(rootPageBlocks));
  }

  private static String findPrerequisitesPageId(BlockList rootPageBlocks) {
    String pageName = "Integration Test Prerequisites";
    return rootPageBlocks.getResults().stream()
        .filter(block -> block.getType().equals("child_page"))
        .filter(block -> block.asChildPage().getChildPage().getTitle().equals(pageName))
        .findFirst()
        .orElseThrow(
            () -> new IllegalStateException("Prerequisites page '" + pageName + "' is not found"))
        .getId();
  }

  private static String getRootPageId() {
    String envValue = System.getenv("IT_NOTION_ROOT_PAGE_ID");
    return envValue != null ? envValue : "2f4cd6cf14068001ac57e261d1c18fda";
  }

  private static String findTestSessionsDatabase(BlockList rootPageBlocks) {
    String databaseName = "Integration Test Sessions";
    return rootPageBlocks.getResults().stream()
        .filter(block -> block.getType().equals("child_database"))
        .filter(block -> block.asChildDatabase().getChildDatabase().getTitle().equals(databaseName))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalStateException(
                    "Test sessions database page '" + databaseName + "' is not found"))
        .getId();
  }

  private static void loadPrerequisites(String pageId) {
    prerequisites = PrerequisitesLoader.load(getNotion(), pageId);
  }

  private static void createTestPage(String databaseId) {
    String name = "Integration tests";
    testPageId = createPageForTests(name, Parent.databaseParent(databaseId));
  }

  public static String createPageForTests(String name) {
    return createPageForTests(name, Parent.pageParent(getInstance().getTestPageId()));
  }

  public static Prerequisites getPrerequisites() {
    return getInstance().prerequisites;
  }

  public static String createPageForTests(String name, Parent parent) {
    CreatePageParams createPageParams = new CreatePageParams();
    createPageParams.setParent(parent);
    createPageParams.setProperties(new HashMap<>());
    createPageParams.getProperties().put(TitleProperty.NAME, NotionProperties.title(name));
    return getNotion().pages().create(createPageParams).getId();
  }

  private String getTestPageId() {
    return testPageId;
  }

  public static File loadFileFailIfMissing(String filePath, ClassLoader classLoader) {
    URL url = classLoader.getResource(filePath);

    if (url == null) {
      fail(
          String.format(
              "File %s should exist in resources/files directory to proceed with the test",
              filePath));
    }

    return new File(url.getFile());
  }
}
