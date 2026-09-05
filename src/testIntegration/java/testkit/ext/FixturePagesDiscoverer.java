package testkit.ext;

import io.kristaxlab.notion.NotionClient;
import io.kristaxlab.notion.fluent.NotionBlocksViewer;
import io.kristaxlab.notion.model.block.Block;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.block.BlockType;
import io.kristaxlab.notion.model.block.ChildPageBlock;
import io.kristaxlab.notion.model.database.Database;
import io.kristaxlab.notion.model.datasource.DataSourcePageList;
import io.kristaxlab.notion.model.page.Page;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Discovers fixture pages within a test session page.
 *
 * <p>Fixture pages are prefilled pages added to the template, named after test ids (e.g. {@code
 * IT-123}). This class searches child pages and rows in the first child database.
 */
public class FixturePagesDiscoverer {

  private static final Logger LOGGER = LoggerFactory.getLogger(FixturePagesDiscoverer.class);

  private final NotionClient notionClient;

  public FixturePagesDiscoverer(NotionClient notionClient) {
    this.notionClient = notionClient;
  }

  /**
   * Discovers fixture pages within the test session page content.
   *
   * <p>Searches for:
   *
   * <ul>
   *   <li>Child pages named after test ids
   *   <li>Database rows (if a database block exists) named after test ids
   * </ul>
   *
   * @param testSessionPageBlocks the blocks of the test session page
   * @return map of test ids to page ids
   */
  public Map<String, String> discoverFixturePages(BlockList testSessionPageBlocks) {
    // TODO move everything to datasource after Notion API supports it, and remove fixture support
    // for standalone pages
    Map<String, String> fixturePages = new HashMap<>(discoverChildPages(testSessionPageBlocks));

    Optional<Block> databaseBlock = findDatabaseBlock(testSessionPageBlocks);
    databaseBlock.ifPresent(block -> fixturePages.putAll(discoverDatabasePages(block.getId())));

    if (!fixturePages.isEmpty()) {
      LOGGER.info(
          "Discovered {} fixture test page(s): {}", fixturePages.size(), fixturePages.keySet());
    }

    return fixturePages;
  }

  /**
   * Finds child pages within the test session page blocks.
   *
   * <p>Child pages named after test ids (e.g. {@code IT-123}) are fixture pages for those tests.
   *
   * @param blocks the test session page blocks
   * @return map of test ids to child page ids
   */
  private Map<String, String> discoverChildPages(BlockList blocks) {
    Map<String, String> childPages = new HashMap<>();

    if (blocks.getResults() == null) {
      return childPages;
    }

    blocks.getResults().stream()
        .filter(b -> BlockType.CHILD_PAGE.getValue().equals(b.getType()))
        .forEach(
            b -> {
              String testId =
                  Optional.ofNullable(b.asChildPage())
                      .map(ChildPageBlock::getChildPage)
                      .map(ChildPageBlock.ChildPage::getTitle)
                      .orElse("")
                      .trim();
              if (Strings.isNotBlank(testId)) {
                childPages.put(testId, b.getId());
                LOGGER.debug("Found fixture child page: {} -> {}", testId, b.getId());
              }
            });

    return childPages;
  }

  /**
   * Finds database pages (rows) that are named after test IDs.
   *
   * <p>Queries all pages in the database and includes those whose title matches a test ID pattern.
   *
   * @param databaseBlockId the ID of the database block
   * @return map of test IDs to database page IDs
   */
  private Map<String, String> discoverDatabasePages(String databaseBlockId) {
    Map<String, String> databasePages = new HashMap<>();

    try {
      Database database = notionClient.databases().retrieve(databaseBlockId);

      if (database.getDataSources() == null || database.getDataSources().isEmpty()) {
        LOGGER.debug("Database {} has no data sources", databaseBlockId);
        return databasePages;
      }

      String dataSourceId = database.getDataSources().get(0).getId();
      LOGGER.info(
          "Searching database {} (data source: {}) for fixture test pages",
          databaseBlockId,
          dataSourceId);

      String cursor = null;
      do {
        DataSourcePageList pages = notionClient.dataSources().query(dataSourceId, cursor, null);
        for (Page page : pages.getResults()) {
          String testId = Optional.ofNullable(page.getTitle()).orElse("").trim();
          if (Strings.isNotBlank(testId)) {
            databasePages.put(testId, page.getId());
            LOGGER.debug("Found fixture database page: {} -> {}", testId, page.getId());
          }
        }
        cursor = Boolean.TRUE.equals(pages.getHasMore()) ? pages.getNextCursor() : null;
      } while (cursor != null);

    } catch (Exception e) {
      LOGGER.warn(
          "Failed to retrieve fixture database pages from block {}: {}",
          databaseBlockId,
          e.getMessage());
    }

    return databasePages;
  }

  /**
   * Finds the first child_database block on the test session page.
   *
   * @param blocks the test session page blocks
   * @return the database block if found
   */
  private Optional<Block> findDatabaseBlock(BlockList blocks) {
    return NotionBlocksViewer.of(blocks)
        .first(b -> BlockType.CHILD_DATABASE.getValue().equals(b.getType()));
  }
}
