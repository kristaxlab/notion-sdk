package integration.helper;

import static org.junit.jupiter.api.Assertions.fail;

import io.kristaxlab.notion.NotionClient;
import java.io.File;
import java.net.URL;

public class IntegrationTestAssisstant {

  private static IntegrationTestAssisstant INSTANCE;

  private static NotionClient getNotion() {
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
