package testkit.ext;

import io.kristaxlab.notion.NotionClient;
import io.kristaxlab.notion.model.page.CreatePageParams;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testkit.ext.client.NotionTestClientProvisioner;
import testkit.util.NotionPageUrlResolver;
import testkit.util.NotionTestIdRetriever;

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
 * <p>The resolved page id will be injected into the parameter marked with {@link TestPageId}
 * annotation.
 */
public class TestPagesProvisioner implements ParameterResolver {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestPagesProvisioner.class);

  private NotionClient notionClient = NotionTestClientProvisioner.getInfraSetupClient();

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.isAnnotated(TestPageId.class)
        && parameterContext.getParameter().getType() == String.class;
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext context)
      throws ParameterResolutionException {
    String sessionPageId = TestSession.get().getSessionPageId();
    Assertions.assertNotNull(sessionPageId, "Test session page ID should not be null");

    String testId = getTestId(context);
    boolean fixtureRequired =
        parameterContext.findAnnotation(TestPageId.class).map(TestPageId::fixture).orElse(false);
    String pageTitle = context.getDisplayName();

    try {
      String notionBaseUrl = NotionPageUrlResolver.getNotionBaseUrl(context);
      String pageId =
          resolvePageId(testId, pageTitle, sessionPageId, fixtureRequired, notionBaseUrl);

      registerTestPage(context, testId, pageId, notionBaseUrl);

      return pageId;
    } catch (Exception e) {
      String message = "Failed to prepare test page for test " + testId + ": " + e.getMessage();
      LOGGER.error(message);
      throw new NotionWorkspaseException(message, e);
    }
  }

  /**
   * Resolves the Notion page ID for the current test, either from a fixture page or by creating a
   * getNotionBaseUrl(context)); } catch (Exception e) { String message = "Failed to prepare test
   * page for test " + testId + ": " + e.getMessage(); LOGGER.error(message); throw new
   * NotionWorkspaseException(message, e); } }
   *
   * <p>/** Resolves the Notion page ID for the current test, either from a fixture page or by
   * creating a dedicated page.
   *
   * @param testId the test ID extracted from the display name
   * @param pageTitle the display name of the test (used for naming dedicated pages)
   * @param sessionPageId the ID of the test session page
   * @param fixtureRequired whether a fixture page is required for this test
   * @return the resolved Notion page ID
   */
  private String resolvePageId(
      String testId,
      String pageTitle,
      String sessionPageId,
      boolean fixtureRequired,
      String notionBaseUrl) {

    Map<String, String> fixturePages = TestSession.get().getFixturePages();
    String fixturePageId = fixturePages.get(testId);

    if (fixturePageId != null) {
      LOGGER.debug("Fixture page is found for test {}", testId);
      return fixturePageId;
    }

    if (fixtureRequired) {
      throw new NotionWorkspaseException(
          "Fixture page for test " + testId + " was not found in the test session page.");
    }

    String dedicatedPageId = createDedicatedPage(sessionPageId, pageTitle);
    LOGGER.debug(
        "Created dedicated page for test {}: {}",
        testId,
        NotionPageUrlResolver.resolveNotionPageUrl(notionBaseUrl, dedicatedPageId));
    return dedicatedPageId;
  }

  private String createDedicatedPage(String sessionPageId, String title) {
    return notionClient
        .pages()
        .create(CreatePageParams.builder().inPage(sessionPageId).title(title).build())
        .getId();
  }

  private String getTestId(ExtensionContext context) {
    return NotionTestIdRetriever.retrieveTestId(context.getDisplayName())
        .orElseThrow(
            () ->
                new NotionWorkspaseException(
                    "Notion Test Id was not found. Check if your test method is annotated with "
                        + "@DisplayName(\"IT-XXX: ...\") annotation to specify a test id of the test"));
  }

  private void registerTestPage(
      ExtensionContext context, String testId, String pageId, String notionBaseUrl) {
    TestPage finalizer = new TestPage(testId, pageId, notionBaseUrl);

    ExtensionContext.Namespace classNamespace =
        ExtensionContext.Namespace.create(context.getRequiredTestClass());
    context.getStore(classNamespace).put(testId, finalizer);
  }
}
