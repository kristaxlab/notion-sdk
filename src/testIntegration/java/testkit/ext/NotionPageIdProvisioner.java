package testkit.ext;

import io.kristaxlab.notion.NotionClient;
import io.kristaxlab.notion.model.page.CreatePageParams;
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
 * Injects a test page. Ensures the test session page singleton first, then creates a child page
 * titled from the method {@code @DisplayName}.
 */
public class NotionPageIdProvisioner implements ParameterResolver {

  private static final Logger LOGGER = LoggerFactory.getLogger(NotionPageIdProvisioner.class);

  private final NotionClient notionClient = NotionTestClientProvisioner.getInfraSetupClient();

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.isAnnotated(NotionPageId.class)
        && parameterContext.getParameter().getType() == String.class;
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext context)
      throws ParameterResolutionException {

    String testId = testId(context);
    String pageTitle = context.getDisplayName();

    try {
      String sessionPageId = TestSession.get(context).ensureTestSessionPage(context);
      String pageId =
          notionClient
              .pages()
              .create(CreatePageParams.builder().inPage(sessionPageId).title(pageTitle).build())
              .getId();
      LOGGER.debug(
          "Created test page for test {}: {}",
          testId,
          NotionPageUrlResolver.resolveNotionPageUrl(context, pageId));
      NotionPage.register(context, testId, pageId);
      return pageId;
    } catch (RuntimeException e) {
      throw new NotionWorkspaseException(
          "Failed to prepare test page for test " + testId + ": " + e.getMessage(), e);
    }
  }

  private static String testId(ExtensionContext context) {
    return NotionTestIdRetriever.retrieveTestId(context.getDisplayName())
        .orElseThrow(
            () ->
                new NotionWorkspaseException(
                    "Notion Test Id was not found. Check if your test method is annotated with "
                        + "@DisplayName(\"IT-XXX: ...\") annotation to specify a test id of the test"));
  }
}
