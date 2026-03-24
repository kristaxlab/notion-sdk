package integration.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import integration.NotionClientProvider;
import io.kristixlab.notion.NotionSdkSettings;
import io.kristixlab.notion.api.NotionClient;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.common.Position;
import io.kristixlab.notion.api.model.common.PositionType;
import io.kristixlab.notion.api.model.pages.CreatePageParams;
import io.kristixlab.notion.api.model.pages.properties.TitleProperty;
import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;

public class IntegrationTestAssisstant {

  public static final String ROOT_PAGE_FOR_TEST_ID = "integration-tests.root-page-id";
  public static final String PREREQUISITES_PAGE_ID = "integration-tests.prerequisites-page-id";
  private static IntegrationTestAssisstant INSTANCE;
  private static Prerequisites prerequisites;
  private static String testPageId;

  public static NotionClient getNotion() {
    return NotionClientProvider.internalTestingClient();
  }

  private static IntegrationTestAssisstant getInstance() {
    if (INSTANCE == null) {
      createInstance();
    }
    return INSTANCE;
  }

  private static synchronized void createInstance() {
    INSTANCE = new IntegrationTestAssisstant();

    loadPrerequisites();
    createTestPage();
  }

  private static void loadPrerequisites() {
    NotionSdkSettings settings = NotionSdkSettings.getInstance();
    prerequisites =
        PrerequisitesLoader.load(
            getNotion(),
            settings.getString(PREREQUISITES_PAGE_ID, "31dcd6cf140680c28ecfe3b1334397df"));
  }

  private static void createTestPage() {
    String parentPageId = NotionSdkSettings.getInstance().getString(ROOT_PAGE_FOR_TEST_ID);
    String name = "Integration tests run " + LocalDateTime.now();
    testPageId = createPageForTests(name, parentPageId);
  }

  public static String getRootPageForTestId() {
    return getInstance().getTestPageId();
  }

  public static String createPageForTests(String name) {
    return createPageForTests(name, getInstance().getTestPageId());
  }

  public static Prerequisites getPrerequisites() {
    return getInstance().prerequisites;
  }

  public static String createPageForTests(String name, String parentPageId) {
    CreatePageParams createPageParams = new CreatePageParams();
    createPageParams.setParent(Parent.pageParent(parentPageId));
    createPageParams.setProperties(new HashMap<>());
    createPageParams.getProperties().put(TitleProperty.NAME, TitleProperty.of(name));
    createPageParams.setPosition(new Position());
    createPageParams.getPosition().setType(PositionType.PAGE_START.getValue());
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

  public static void checkThatExists(NotionSdkSettings settings, Class clazz, String key) {
    String value = settings.getString(key);
    assertNotNull(
        value, String.format("Property '%s' is missing in settings for %s", key, clazz.getName()));
  }
}
