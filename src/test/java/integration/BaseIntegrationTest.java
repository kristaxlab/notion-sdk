package integration;

import integration.helper.NotionTestClientProvider;
import io.kristixlab.notion.api.NotionClient;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInfo;

/**
 * Provides common setup for integration tests that use a {@link NotionClient}.
 *
 * <p>Before each test, this base class creates a client configured to write HTTP exchange logs
 * under a deterministic directory derived from the test class and method names.
 */
@Tag("integration")
public abstract class BaseIntegrationTest {

  private NotionClient notionClient;

  /**
   * Initializes the integration test client with a per-test exchange log directory.
   *
   * @param testInfo metadata for the currently executing test, used to derive log directory names
   */
  @BeforeEach
  protected void beforeEach(TestInfo testInfo) {
    String testClass = testInfo.getTestClass().map(Class::getSimpleName).orElse("unknownClass");

    String testMethod = sanitize(testInfo.getDisplayName());
    if (testMethod.isEmpty()) {
      testMethod = testInfo.getTestMethod().map(Method::getName).orElse("unknownMethod");
    }

    Path exchangeDir = Paths.get("exchanges", "exchange-logs", testClass, testMethod);

    notionClient = NotionTestClientProvider.internalTestingClient(exchangeDir);
  }

  /**
   * Converts a candidate file or directory name into a filesystem-safe identifier.
   *
   * <p>Illegal path characters are replaced with underscores, whitespace and dot runs are
   * normalized to a single underscore, repeated underscores are collapsed, and leading/trailing
   * underscores are removed.
   *
   * @param name input value to sanitize
   * @return sanitized identifier, or an empty string when {@code name} is {@code null}
   */
  private static String sanitize(String name) {
    if (name == null) return "";
    return name.replaceAll("[/\\\\:*?\"<>|()\\[\\]]", "_")
        .replaceAll("[\\s.]+", "_")
        .replaceAll("_+", "_")
        .replaceAll("^_|_$", "");
  }

  /**
   * Returns the client configured for the current integration test.
   *
   * @return the initialized {@link NotionClient} instance
   */
  public NotionClient getNotion() {
    return notionClient;
  }
}
