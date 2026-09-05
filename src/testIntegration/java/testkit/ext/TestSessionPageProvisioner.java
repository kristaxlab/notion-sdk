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
 * Provisions test session pages with template support and fixture discovery.
 *
 * <p>This class handles the complete provisioning flow:
 *
 * <ol>
 *   <li>Creates a session page from the configured parent and template
 *   <li>Waits for template content to be applied by Notion
 *   <li>Discovers any prefilled fixture pages in the template
 *   <li>Packages everything into immutable session data
 * </ol>
 *
 * <p>Templates are applied asynchronously by Notion, so this class polls for content to appear
 * before proceeding with fixture discovery.
 */
public class TestSessionPageProvisioner {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestSessionPageProvisioner.class);

  private static final PollingConfig TEMPLATE_POLLING =
      PollingConfig.of(Duration.ofSeconds(15), Duration.ofMillis(500));

  private final NotionClient notionClient;
  private final FixturePagesDiscoverer fixtureDiscoverer;

  /**
   * Creates a session page provisioner with the specified dependencies.
   *
   * @param notionClient the Notion client for API operations
   * @param fixtureDiscoverer discovers prefilled fixture pages in session content
   */
  public TestSessionPageProvisioner(
      NotionClient notionClient, FixturePagesDiscoverer fixtureDiscoverer) {
    this.notionClient = notionClient;
    this.fixtureDiscoverer = fixtureDiscoverer;
  }

  /**
   * Provisions a new test session from the given configuration.
   *
   * <p>Creates a session page, waits for template content, discovers fixtures, and packages
   * everything into session data.
   *
   * @param config the session configuration
   * @return immutable session data containing page ID, bot user ID, and fixtures
   */
  public TestSession.Data provision(TestSessionConfig config) {
    LOGGER.info("Provisioning test session from configuration");

    Page sessionPage = createSessionPage(config);

    BlockList blocks =
        TemplatePoller.awaitAnyBlocks(notionClient, sessionPage.getId(), TEMPLATE_POLLING);
    Map<String, String> fixturePages = fixtureDiscoverer.discoverFixturePages(blocks);

    TestSession.Data sessionData =
        new TestSession.Data(sessionPage.getId(), sessionPage.getCreatedBy().getId(), fixturePages);

    LOGGER.info(
        "Session provisioned: page={}, fixtures={}", sessionPage.getId(), fixturePages.size());

    return sessionData;
  }

  /**
   * Creates a test session page with the specified name and template.
   *
   * @param config
   * @return the created page
   */
  private Page createSessionPage(TestSessionConfig config) {

    String parentId = config.getParentId();
    String templateId = config.getTemplateId();
    String title =
        config.getSessionTitle() == null ? "Integration tests session" : config.getSessionTitle();

    LOGGER.info(
        "Creating test session page in {}, template={}, name={}", parentId, templateId, title);

    Parent parent = resolveParent(parentId);
    TemplateParams template = resolveTemplate(templateId, parent);

    Page sessionPage =
        notionClient.pages().create(page -> page.title(title).parent(parent).template(template));
    LOGGER.info("Test session page created: {}", sessionPage.getId());
    return sessionPage;
  }

  /**
   * Determines whether the configured parent ID refers to a data source or database.
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
   * Resolves the template to use for page creation.
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
