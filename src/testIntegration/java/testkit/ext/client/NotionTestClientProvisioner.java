package testkit.ext.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kristaxlab.notion.NotionClient;
import io.kristaxlab.notion.config.ConfigurationLookup;
import io.kristaxlab.notion.http.base.interceptor.ExchangeRecordingInterceptor;
import io.kristaxlab.notion.http.base.json.JacksonSerializer;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testkit.util.PathSanitizer;
import testkit.util.TestConfigurationLookup;

/**
 * Resolves a {@link NotionClient} for a parameter marked {@link NotionTestClient}.
 *
 * <p>The Notion Test Http Client records exchanges under a {@link PathSanitizer sanitized} test
 * class name and honours {@code notion.tests.json.strict}. The setup client ({@link
 * NotionTestClient#forSetup()} {@code true} and {@link #getInfraSetupClient()}) always uses
 * non-strict JSON and logs under {@code test-logs/rqrs/setup}.
 */
public class NotionTestClientProvisioner implements ParameterResolver {

  private static final Logger LOGGER = LoggerFactory.getLogger(NotionTestClientProvisioner.class);
  private static final String NOTION_TESTS_AUTH_TOKEN = "NOTION_TESTS_AUTH_TOKEN";
  private static final String NOTION_TESTS_JSON_STRICT = "notion.tests.json.strict";
  private static final String BASE_LOGS_DIR = "test-logs/rqrs";

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

    boolean strictJson = TestConfigurationLookup.lookupBoolean(NOTION_TESTS_JSON_STRICT, context);
    return internalTestingClient(logPath, "Notion Client", strictJson);
  }

  private String resolveTestClassName(ExtensionContext context) {
    String raw = context.getTestClass().map(Class::getSimpleName).orElse("unknownClass");
    String sanitized = PathSanitizer.sanitize(raw);
    return sanitized.isEmpty() ? "unknownClass" : sanitized;
  }

  private boolean isForSetup(ParameterContext parameterContext) {
    return parameterContext
        .findAnnotation(NotionTestClient.class)
        .map(NotionTestClient::forSetup)
        .orElse(false);
  }

  /**
   * Returns the setup client used by provisioners. Logs under {@code test-logs/rqrs/setup} and
   * never uses strict JSON.
   *
   * @return the setup client
   */
  public static NotionClient getInfraSetupClient() {
    return internalTestingClient(Path.of(BASE_LOGS_DIR, "setup"), "Notion Test Env Setup", false);
  }

  /**
   * Creates a {@link NotionClient} for integration tests.
   *
   * <p>When {@code exchangeLogDir} is non-{@code null}, an {@link ExchangeRecordingInterceptor}
   * writes each request/response pair as a JSON file into that directory.
   *
   * @param exchangeLogDir target directory for HTTP exchange files; {@code null} disables exchange
   *     logging
   * @param clientName display name for the client
   * @param strictJson when {@code true}, unknown JSON properties fail deserialization
   * @return a {@link NotionClient} authenticated with {@code NOTION_TESTS_AUTH_TOKEN}
   * @throws IllegalStateException if {@code NOTION_TESTS_AUTH_TOKEN} is absent or blank
   */
  public static NotionClient internalTestingClient(
      Path exchangeLogDir, String clientName, boolean strictJson) {
    String apiKey =
        ConfigurationLookup.lookup(NOTION_TESTS_AUTH_TOKEN)
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        NOTION_TESTS_AUTH_TOKEN + " environment variable is not set"));

    clientName = (clientName == null || clientName.isBlank()) ? "Notion Client" : clientName;
    return NotionClient.builder()
        .authToken(apiKey)
        .jsonSerializer(serializer(strictJson))
        .exchangeLogging(exchangeLogDir)
        .clientName(clientName)
        .build();
  }

  private static JacksonSerializer serializer(boolean strictJson) {
    ObjectMapper mapper = JacksonSerializer.defaultMapper();
    if (strictJson) {
      LOGGER.debug("Enabling strict JSON deserialization for Notion test client");
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }
    return new JacksonSerializer(mapper);
  }
}
