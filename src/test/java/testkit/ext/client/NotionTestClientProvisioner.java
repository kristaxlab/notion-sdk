package testkit.ext.client;

import io.kristaxlab.notion.NotionClient;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testkit.ext.TestPage;
import testkit.ext.TestSessionBeforeAll;

/**
 * Resolves the Notion page each test runs on within the test session provisioned by {@link
 * TestSessionBeforeAll}, in the following order:
 *
 * <ol>
 *   <li>a prefilled page added by the test session template and named after the test id (ex.
 *       IT-123) - allows setting up prerequisites that are not possible to set through API;
 *   <li>a dedicated page created under the test session page.
 * </ol>
 *
 * <p>The resolved page id will be injected into the parameter marked with {@link TestPage}
 * annotation.
 */
public class NotionTestClientProvisioner implements ParameterResolver {

  private static final Logger LOGGER = LoggerFactory.getLogger(NotionTestClientProvisioner.class);

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.isAnnotated(NotionTestClient.class)
        && parameterContext.getParameter().getType() == NotionClient.class;
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext context)
      throws ParameterResolutionException {

    boolean logExchanges =
        parameterContext
            .findAnnotation(NotionTestClient.class)
            .map(NotionTestClient::logExchanges)
            .orElse(false);

    if (!logExchanges) {
      LOGGER.debug(
          "Resolving Notion test client for test class {} without exchange logging",
          context.getTestClass().map(Class::getSimpleName).orElse("unknownClass"));
      return NotionTestClientProvider.internalTestingClient(null, "Notion Client");
    }

    String testClass = context.getTestClass().map(Class::getSimpleName).orElse("unknownClass");
    Path exchangeDir = Paths.get("exchanges", "exchange-logs", testClass);
    LOGGER.debug(
        "Resolving Notion test client for test class {} with exchange log directory {}",
        testClass,
        exchangeDir);

    return NotionTestClientProvider.internalTestingClient(exchangeDir, "Notion Client");
  }
}
