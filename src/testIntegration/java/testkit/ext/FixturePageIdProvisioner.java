package testkit.ext;

import java.util.Map;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import testkit.util.NotionTestIdRetriever;

/**
 * Injects the fixture page for the current test id. Discovers fixture pages on the test session
 * page first; fails if that id is missing.
 */
public class FixturePageIdProvisioner implements ParameterResolver {

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.isAnnotated(FixtureNotionPageId.class)
        && parameterContext.getParameter().getType() == String.class;
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext context)
      throws ParameterResolutionException {
    String testId = testId(context);
    try {
      Map<String, String> fixturePages = TestSession.get(context).ensureFixtures(context);
      String fixturePageId = fixturePages.get(testId);
      if (fixturePageId == null) {
        throw new NotionWorkspaseException(
            "Fixture page for test " + testId + " was not found on the test session page.");
      }
      NotionPage.register(context, testId, fixturePageId);
      return fixturePageId;
    } catch (RuntimeException e) {
      throw new NotionWorkspaseException(
          "Failed to prepare fixture page for test " + testId + ": " + e.getMessage(), e);
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
