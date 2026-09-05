package testkit.ext;

import io.kristaxlab.notion.NotionClient;
import io.kristaxlab.notion.http.error.NotFoundException;
import io.kristaxlab.notion.http.error.ValidationException;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.common.Parent;
import io.kristaxlab.notion.model.database.Database;
import io.kristaxlab.notion.model.datasource.DataSource;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.templates.TemplateParams;
import io.kristaxlab.notion.util.PollingConfig;
import io.kristaxlab.notion.util.TemplatePoller;
import java.time.Duration;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates the test session page and discovers fixture pages under it.
 *
 * <p>Templates are applied asynchronously by Notion, so fixture discovery polls for content first.
 */
public class TestSessionPageProvisioner {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestSessionPageProvisioner.class);

  private static final PollingConfig TEMPLATE_POLLING =
      PollingConfig.of(Duration.ofSeconds(15), Duration.ofMillis(500));
  private static final String DEFAULT_TITLE = "Integration tests session";

  private final NotionClient notionClient;
  private final FixturePagesDiscoverer fixtureDiscoverer;

  /**
   * Creates a test session page provisioner with the specified dependencies.
   *
   * @param notionClient the Notion client for API operations
   * @param fixtureDiscoverer discovers fixture pages on the test session page
   */
  public TestSessionPageProvisioner(
      NotionClient notionClient, FixturePagesDiscoverer fixtureDiscoverer) {
    this.notionClient = notionClient;
    this.fixtureDiscoverer = fixtureDiscoverer;
  }

  /**
   * Creates the test session page under the Test Sessions Home. When the home is a database, Notion
   * applies the default template unless a template id is configured.
   *
   * @return the created page id
   */
  public String createTestSessionPage(TestSessionConfig config) {
    Parent parent = resolveParent(config.getParentId());
    TemplateParams template = resolveTemplate(config.getTemplateId(), parent);
    Page page = createPage(config, sessionTitle(config), template, parent);
    LOGGER.info("Test session page created: {}", page.getId());
    return page.getId();
  }

  /**
   * Waits for template content on the test session page and discovers fixture pages.
   *
   * @return test id → page id
   */
  public Map<String, String> discoverFixtures(String testSessionPageId) {
    BlockList blocks =
        TemplatePoller.awaitAnyBlocks(notionClient, testSessionPageId, TEMPLATE_POLLING);
    Map<String, String> fixturePages = fixtureDiscoverer.discoverFixturePages(blocks);
    LOGGER.info(
        "Fixtures discovered: page={}, fixtures={}", testSessionPageId, fixturePages.size());
    return Map.copyOf(fixturePages);
  }

  private Page createPage(
      TestSessionConfig config, String title, TemplateParams template, Parent parent) {
    LOGGER.info("Creating page in {}, template={}, name={}", config.getParentId(), template, title);

    return notionClient.pages().create(page -> page.title(title).parent(parent).template(template));
  }

  private static String sessionTitle(TestSessionConfig config) {
    return config.getSessionTitle() == null ? DEFAULT_TITLE : config.getSessionTitle();
  }

  /**
   * Determines whether the Test Sessions Home id refers to a data source or database.
   *
   * <p>The Notion API has no single endpoint telling what kind of object an ID refers to, so we try
   * to retrieve it as a data source first, then fall back to database.
   */
  private Parent resolveParent(String parentId) {
    try {
      DataSource dataSource = notionClient.dataSources().retrieve(parentId);
      LOGGER.info("Parent {} is a data source", parentId);
      return Parent.dataSourceParent(dataSource.getId());
    } catch (NotFoundException | ValidationException e) {
      Database database = notionClient.databases().retrieve(parentId);
      LOGGER.info("Parent {} is a database", parentId);
      return Parent.databaseParent(database.getId());
    }
  }

  /**
   * Resolves the template to use for the test session page.
   *
   * <p>An explicit template ID works for any parent (Notion duplicates the referenced page), while
   * the "default" template is a property of a database. When the parent is a database and no
   * explicit template ID is configured, the default template is used.
   */
  private TemplateParams resolveTemplate(String templateId, Parent parent) {
    if (templateId != null && !"default".equals(templateId)) {
      return TemplateParams.templateId(templateId);
    }
    return parent.getDatabaseId() != null ? TemplateParams.defaultTemplate() : null;
  }
}
