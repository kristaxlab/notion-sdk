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

  private static final Duration TEMPLATE_APPLY_TIMEOUT = Duration.ofSeconds(15);
  private static final Duration TEMPLATE_POLL_INTERVAL = Duration.ofMillis(500);

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
   * @throws InterruptedException if waiting for template content is interrupted
   */
  public TestSession.Data provision(TestSessionConfig config) throws InterruptedException {
    LOGGER.info("Provisioning test session from configuration");

    Page sessionPage =
        createSessionPage(config.getParentId(), config.getTemplateId(), config.getSessionTitle());

    BlockList blocks = waitForTemplateContent(sessionPage.getId());
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
   * @param parentId the ID of the parent page, database, or data source
   * @param templateId the template ID to use, "default" for database default template, or null
   * @param pageName the name for the session page
   * @return the created page
   */
  private Page createSessionPage(String parentId, String templateId, String pageName) {
    LOGGER.info(
        "Creating test session page in {}, template={}, name={}", parentId, templateId, pageName);

    Parent parent = resolveParent(parentId);
    TemplateParams template = resolveTemplate(templateId, parent);

    String title =
        (pageName == null || pageName.isBlank()) ? "Integration tests session" : pageName;

    return notionClient.pages().create(page -> page.title(title).parent(parent).template(template));
  }

  /**
   * Waits for template content to be applied to a newly created page.
   *
   * <p>Notion applies page templates asynchronously, so right after page creation its content may
   * not be there yet. This method polls the page children every {@link #TEMPLATE_POLL_INTERVAL}
   * until any content appears or {@link #TEMPLATE_APPLY_TIMEOUT} elapses.
   *
   * @param pageId the ID of the page to wait for
   * @return the page's child blocks (may be empty if template has no content)
   * @throws InterruptedException if interrupted while waiting
   */
  private BlockList waitForTemplateContent(String pageId) throws InterruptedException {
    long deadline = System.currentTimeMillis() + TEMPLATE_APPLY_TIMEOUT.toMillis();
    BlockList blocks = notionClient.blocks().retrieveChildren(pageId);

    while (blocks.getResults() == null || blocks.getResults().isEmpty()) {
      if (System.currentTimeMillis() >= deadline) {
        LOGGER.warn(
            "Test session page {} still has no content after {}; proceeding assuming the template is empty",
            pageId,
            TEMPLATE_APPLY_TIMEOUT);
        break;
      }
      Thread.sleep(TEMPLATE_POLL_INTERVAL.toMillis());
      blocks = notionClient.blocks().retrieveChildren(pageId);
    }

    return blocks;
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
