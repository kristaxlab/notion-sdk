package testkit.ext.client;

import static io.kristaxlab.notion.NotionTestEnvironmentConstants.NOTION_TEST_AUTH_TOKEN;

import io.kristaxlab.notion.NotionClient;
import io.kristaxlab.notion.config.ConfigurationLookup;
import io.kristaxlab.notion.http.base.interceptor.ExchangeRecordingInterceptor;
import io.kristaxlab.notion.http.base.json.TestSerializer;
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
  private static final String BASE_LOGS_DIR = "notion-tests-rqrs-logs";

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

    boolean isForSetup = isForSetup(parameterContext);
    if (isForSetup) {
      return getInfraSetupClient();
    }

    String testClassName = resolveTestClassName(context);
    Path logPath = Paths.get(BASE_LOGS_DIR, testClassName);
    LOGGER.debug(
        "Resolving Notion test client for test class {} with log directory {}",
        testClassName,
        logPath);

    return internalTestingClient(logPath, "Notion Client");
  }

  private String resolveTestClassName(ExtensionContext context) {
    return context.getTestClass().map(Class::getSimpleName).orElse("unknownClass");
  }

  private boolean isForSetup(ParameterContext parameterContext) {
    return parameterContext
        .findAnnotation(NotionTestClient.class)
        .map(NotionTestClient::forSetup)
        .orElse(false);
  }

  /**
   * Returns a {@link NotionClient} for internal integration tests that is configured to log HTTP
   * exchanges to a separate directory no to confuse with logs related to the actual test execution.
   *
   * <p>This client is intended for use in infrastructure setup tasks, such as provisioning a test
   * session page.
   *
   * @return
   */
  public static NotionClient getInfraSetupClient() {
    return internalTestingClient(Path.of("notion-test-log/setup"), "Notion Test Env Setup");
  }

  /**
   * Creates a {@link NotionClient} for internal integration tests.
   *
   * <p>When {@code exchangeLogDir} is non-{@code null}, an {@link ExchangeRecordingInterceptor} is
   * added to the HTTP pipeline and writes each request/response pair as a pretty-printed JSON file
   * into the given directory. The directory is created automatically if it does not exist.
   *
   * @param exchangeLogDir target directory for HTTP exchange files; {@code null} disables exchange
   *     logging
   * @return a fully-wired {@link NotionClient} backed by the NOTION_TEST_AUTH_TOKENtoken
   * @throws IllegalStateException if the NOTION_TEST_AUTH_TOKEN environment variable is absent or
   *     blank
   */
  public static NotionClient internalTestingClient(Path exchangeLogDir, String clientName) {
    String apiKey =
        ConfigurationLookup.lookup(NOTION_TEST_AUTH_TOKEN)
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        NOTION_TEST_AUTH_TOKEN + " environment variable is not set"));

    clientName = (clientName == null || clientName.isBlank()) ? "Notion Client" : clientName;
    return NotionClient.builder()
        .authToken(apiKey)
        .jsonSerializer(new TestSerializer()) // strict serializer
        .exchangeLogging(exchangeLogDir)
        .clientName(clientName)
        .build();
  }
}
