package integration.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import io.kristixlab.notion.NotionSdkSettings;
import java.io.File;
import java.net.URL;

public class IntegrationTestUtil {

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
