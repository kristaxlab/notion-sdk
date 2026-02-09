package integration;

import static org.junit.jupiter.api.Assertions.fail;

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
}
