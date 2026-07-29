package integration.extension;

import integration.helper.NotionTestClientProvider;
import integration.helper.TestAwareConfigurationLookup;
import io.kristaxlab.notion.NotionClient;
import io.kristaxlab.notion.fluent.NotionBlocksViewer;
import io.kristaxlab.notion.model.block.Block;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.block.BlockType;
import io.kristaxlab.notion.model.block.ChildPageBlock;
import io.kristaxlab.notion.model.common.Parent;
import io.kristaxlab.notion.model.common.ParentType;
import io.kristaxlab.notion.model.database.Database;
import io.kristaxlab.notion.model.page.CreatePageParams;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.templates.TemplateParams;
import org.apache.logging.log4j.util.Strings;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Creates a dedicated Notion page for the ongoing test session {@link #beforeAll}.
 *
 * <p>And before each test creates a dedicated Notion page for this particular test (checks first
 * for a prefilled page that could be added within creating a tests session page, this possibility
 * allows for setting up prerequisites that are not possible to set through API). Test page id will
 * be injected into the field marked with {@link NotionTestPage} annotation
 *
 * <p>
 *
 * <p>Required configuration parameter: {@link TESTS_HOME_ID} - may be set via environment variable,
 * system or junit property
 */
public class NotionTestPagesProvisioner implements BeforeAllCallback, AfterEachCallback, ParameterResolver {

  private static AtomicBoolean rootTestPageCreated = new AtomicBoolean(false);
  private static NotionClient notionClient;

  private static final Logger LOGGER = LoggerFactory.getLogger(NotionTestPagesProvisioner.class);
  private static final String TESTS_HOME_ID = "notion.tests.home.id";
  private static final String TEMPLATE_ID = "notion.tests.template.id";
  private static final String PAGE_NAME = "notion.tests.page.name";
  private static final String PAGE_BASE_URL = "notion.tests.page.base.url";
  private static final String PAGE_CLEANUP = "notion.tests.page.cleanup.enabled";

  static {
    notionClient = NotionTestClientProvider.getInfraSetupClient();
  }

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {
    // TODO this is not a notion page extension but rather notion test session context, move to
    // another class
    if (rootTestPageCreated.compareAndSet(false, true)) {
      Page testSessionPage = createRootTestPage(context);

      // TODO Template needs time to apply
      Thread.sleep(2000);
      Map<String, String> preAddedPages = null;
      BlockList testHomeBlocks = notionClient.blocks().retrieveChildren(testSessionPage.getId());
      Optional<Block> databaseBlock =
              NotionBlocksViewer.of(testHomeBlocks)
                      .first(b -> BlockType.CHILD_DATABASE.getValue().equals(b.getType()));
      if (databaseBlock.isPresent()) {
        Database database = notionClient.databases().retrieve(databaseBlock.get().getId());
        if (database.getDataSources() != null && !database.getDataSources().isEmpty()) {
          LOGGER.info(
                  "Test session page {} contains a database {}. Setting this database as home for"
                          + " tests of current test session",
                  testSessionPage.getId(),
                  databaseBlock.get().getId());
          String dataSourceId = database.getDataSources().get(0).getId();
          // TODO implement lookup of pages in a database or data source with query endpoint
        }
      }

      preAddedPages = retrievePreAddedPages(testHomeBlocks);
      NotionTestContext.initialize(
              testSessionPage.getId(), preAddedPages, testSessionPage.getCreatedBy().getId());
    }
  }

  /**
   * Creates a "home" Notion page for running all the tests
   *
   * @param context {@link ExtensionContext}
   * @return id of the Notion page created
   */
  private Page createRootTestPage(ExtensionContext context) {
    LOGGER.debug("Reading test properties before preparing Notion workspace for running tests");

    String testsHomeId = lookupForProp(TESTS_HOME_ID, context, true);
    String templateId = lookupForProp(TEMPLATE_ID, context, false);
    String pageName = lookupForProp(PAGE_NAME, context, false);

    LOGGER.info(
            "Creating Notion page for tests in {}, template={}, name={}",
            testsHomeId,
            templateId,
            pageName);

    // TODO implement dynamic choice between database / datasource and page - retrieve with Notion
    // search and then
    // set parent dynamically and check templateId if it is possible
    Parent testsParent = Parent.of(testsHomeId, ParentType.DATABASE);

    TemplateParams template =
            (templateId == null || "default".equals(templateId))
                    ? TemplateParams.defaultTemplate()
                    : TemplateParams.templateId(templateId);

    return notionClient
            .pages()
            .create(
                    page ->
                            page.title(StringUtils.isBlank(pageName) ? "Integration tests session" : pageName)
                                    .parent(testsParent)
                                    .template(template));
  }

  /**
   * Looks for pre added pages within the "home" Notion page dedicated for current test session. Pre
   * added pages are supposed to be added to the template that is used for creating a "home" page.
   * Pre added pages help to test scenarious which require prerequisites that are only possible to
   * be added via Notion UI.
   *
   * @param blocks list of Notion blocks that may contain pre added test pages
   * @return map of test id (like IT-123) to the corresponding Notion page id.
   */
  private Map<String, String> retrievePreAddedPages(BlockList blocks) {
    Map<String, String> preAddedPages = new HashMap<>();

    blocks.getResults().stream()
            .filter(b -> BlockType.CHILD_PAGE.getValue().equals(b.getType()))
            .forEach(
                    // preadded pages should be named according to corresponding tests (ex. IT-123)
                    b -> {
                      String testId =
                              Optional.ofNullable(b.asChildPage())
                                      .map(ChildPageBlock::getChildPage)
                                      .map(ChildPageBlock.ChildPage::getTitle)
                                      .orElse("")
                                      .trim();
                      if (Strings.isNotBlank(testId)) {
                        preAddedPages.put(testId, b.getId());
                      }
                    });
    return preAddedPages;
  }

  /**
   * Looks up for property value (in environment, system and junit properties)
   *
   * @param key      property key
   * @param context  {@link ExtensionContext}
   * @param required if set to true an exception will be thrown if no property is found
   * @return property value or null
   */
  private @NonNull String lookupForProp(String key, ExtensionContext context, boolean required) {
    Optional<String> value = TestAwareConfigurationLookup.lookup(key, context);
    LOGGER.debug("{}: {}", key, value.orElse(null));

    if (required && value.isEmpty()) {
      throw new IllegalStateException("Required property " + key + " is missing");
    }
    return value.orElse(null);
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    String currentTestPageId = NotionTestContext.getInstance().getCurrentTestPageId();
    if (currentTestPageId == null) {
      return;
    }

    String testPageUrl = toNotionPageUrl(context, currentTestPageId);
    LOGGER.info("Completed {}. Notion test page: {}", context.getDisplayName(), testPageUrl);
    NotionTestContext.getInstance().clearCurrentTestPageId();
  }


  public static String toNotionPageUrl(ExtensionContext context, String pageId) {
    String baseUrl = context.getConfigurationParameter(PAGE_BASE_URL).orElse("https://www.notion.so/");
    return baseUrl + pageId.replace("-", "");
  }


  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.isAnnotated(NotionTestPage.class) && parameterContext.getParameter().getType() == String.class;
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext context) throws ParameterResolutionException {
    // TODO thread safety - reading here should be guaranteed to happen AFTER
    //  the previous block is fully executed at least once (blocking read operation to get test id
    // and templates map?)

    String testSessionPageId = NotionTestContext.getInstance().getRootTestPageId();
    Assertions.assertNotNull(testSessionPageId, "Test session page ID should not be null");

    String testId = getTestId(context);
    String testPageId = NotionTestContext.getInstance().getPrefilledPages().get(testId);
    if (testPageId == null) {
      testPageId = createTestPage(testSessionPageId, context.getDisplayName());
    }
    NotionTestContext.getInstance().setCurrentTestPageId(testPageId);
    return testPageId;
  }

  private String createTestPage(String testSessionPageId, String title) {
    String testClassPageId = notionClient
            .pages()
            .create(CreatePageParams.builder()
                    .inPage(testSessionPageId)
                    .title(title)
                    .build())
            .getId();

    return testClassPageId;
  }

  private String getTestId(ExtensionContext context) {
    Pattern pattern = Pattern.compile("(?i)\\bIT-\\d+\\b");
    Matcher matcher = pattern.matcher(context.getDisplayName());
    if (matcher.find()) {
      return matcher.group(0).toUpperCase();
    }
    throw new NotionFixtureException("Notion Test Id was not found. Mark test method with " +
            "@DisplayName(\"IT-XXX: ...\") annotation to specify a test id for the test");
  }

}
