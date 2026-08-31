package io.kristaxlab.notion.util;

import io.kristaxlab.notion.NotionClient;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.page.Page;
import java.time.Duration;
import java.util.function.Predicate;

/**
 * Utility for polling pages and blocks until a template has been fully applied.
 *
 * <p>Notion applies templates asynchronously, so immediately after creating a page with a template,
 * the template content may not yet be present. This utility polls the page or its blocks until a
 * user-defined condition is met, or until a timeout/max attempts is reached.
 *
 * <p><b>Example usage - wait for specific number of blocks:</b>
 *
 * <pre>{@code
 * Page page = client.pages().create(p ->
 *     p.inPage(parentId).template(TemplateParams.templateId(templateId)));
 *
 * // Wait up to 10 seconds for 3 blocks to appear
 * Page updated = TemplatePoller.awaitPage(
 *     client,
 *     page.getId(),
 *     p -> client.blocks().retrieveChildren(p.getId()).getResults().size() == 3,
 *     PollingConfig.ofTimeout(Duration.ofSeconds(10))
 * );
 * }</pre>
 *
 * <p><b>Example usage - wait for specific property value:</b>
 *
 * <pre>{@code
 * Page updated = TemplatePoller.awaitPage(
 *     client,
 *     page.getId(),
 *     p -> {
 *         var titleProp = p.getProperties().get("Title");
 *         return titleProp != null && !titleProp.getTitle().isEmpty();
 *     },
 *     PollingConfig.of(Duration.ofSeconds(10), Duration.ofMillis(500))
 * );
 * }</pre>
 */
public class TemplatePoller {

  private TemplatePoller() {}

  /**
   * Polls a page until a readiness condition is met.
   *
   * @param client Notion client for API calls
   * @param pageId page identifier to poll
   * @param readinessCheck predicate that returns true when the page is ready
   * @param config polling configuration (timeout, attempts, interval)
   * @return the page when ready
   * @throws TemplatePollingException if timeout, max attempts reached, or polling is interrupted
   */
  public static Page awaitPage(
      NotionClient client, String pageId, Predicate<Page> readinessCheck, PollingConfig config) {

    validateConfig(config);

    long startTime = System.currentTimeMillis();
    Duration timeout = config.getTimeout();
    Integer maxAttempts = config.getMaxAttempts();

    Page lastPage = null;
    int attempt = 0;

    while (true) {
      attempt++;

      // Check max attempts
      if (maxAttempts != null && attempt > maxAttempts) {
        throw new TemplatePollingException(
            String.format(
                "Template polling exceeded max attempts (%d) for page %s", maxAttempts, pageId));
      }

      // Check timeout
      if (timeout != null) {
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed > timeout.toMillis()) {
          throw new TemplatePollingException(
              String.format("Template polling timed out after %dms for page %s", elapsed, pageId));
        }
      }

      // Retrieve and check page
      lastPage = client.pages().retrieve(pageId);
      if (readinessCheck.test(lastPage)) {
        return lastPage;
      }

      sleep(config.getPollingInterval(), "page " + pageId);
    }
  }

  /**
   * Polls page blocks until a readiness condition is met.
   *
   * @param client Notion client for API calls
   * @param pageId page identifier whose blocks to poll
   * @param readinessCheck predicate that returns true when blocks are ready
   * @param config polling configuration (timeout, attempts, interval)
   * @return the block list when ready
   * @throws TemplatePollingException if timeout, max attempts reached, or polling is interrupted
   */
  public static BlockList awaitBlocks(
      NotionClient client,
      String pageId,
      Predicate<BlockList> readinessCheck,
      PollingConfig config) {

    validateConfig(config);

    long startTime = System.currentTimeMillis();
    Duration timeout = config.getTimeout();
    Integer maxAttempts = config.getMaxAttempts();

    BlockList lastBlocks = null;
    int attempt = 0;

    while (true) {
      attempt++;

      // Check max attempts
      if (maxAttempts != null && attempt > maxAttempts) {
        throw new TemplatePollingException(
            String.format(
                "Template polling exceeded max attempts (%d) for page blocks %s",
                maxAttempts, pageId));
      }

      // Check timeout
      if (timeout != null) {
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed > timeout.toMillis()) {
          throw new TemplatePollingException(
              String.format(
                  "Template polling timed out after %dms for page blocks %s", elapsed, pageId));
        }
      }

      // Retrieve and check blocks
      lastBlocks = client.blocks().retrieveChildren(pageId);
      if (readinessCheck.test(lastBlocks)) {
        return lastBlocks;
      }

      sleep(config.getPollingInterval(), "page blocks " + pageId);
    }
  }

  /**
   * Convenience method: waits for a specific number of blocks to appear on a page.
   *
   * @param client Notion client for API calls
   * @param pageId page identifier whose blocks to poll
   * @param expectedBlockCount expected number of blocks
   * @param config polling configuration
   * @return the block list when ready
   * @throws TemplatePollingException if timeout, max attempts reached, or polling is interrupted
   */
  public static BlockList awaitBlockCount(
      NotionClient client, String pageId, int expectedBlockCount, PollingConfig config) {
    return awaitBlocks(
        client,
        pageId,
        blocks -> blocks.getResults() != null && blocks.getResults().size() == expectedBlockCount,
        config);
  }

  /**
   * Convenience method: waits for at least a minimum number of blocks to appear on a page.
   *
   * @param client Notion client for API calls
   * @param pageId page identifier whose blocks to poll
   * @param minBlockCount minimum number of blocks
   * @param config polling configuration
   * @return the block list when ready
   * @throws TemplatePollingException if timeout, max attempts reached, or polling is interrupted
   */
  public static BlockList awaitMinBlockCount(
      NotionClient client, String pageId, int minBlockCount, PollingConfig config) {
    return awaitBlocks(
        client,
        pageId,
        blocks -> blocks.getResults() != null && blocks.getResults().size() >= minBlockCount,
        config);
  }

  /**
   * Convenience method: waits for any blocks to appear on a page (at least 1 block).
   *
   * @param client Notion client for API calls
   * @param pageId page identifier whose blocks to poll
   * @param config polling configuration
   * @return the block list when ready
   * @throws TemplatePollingException if timeout, max attempts reached, or polling is interrupted
   */
  public static BlockList awaitAnyBlocks(NotionClient client, String pageId, PollingConfig config) {
    return awaitMinBlockCount(client, pageId, 1, config);
  }

  private static void sleep(Duration interval, String context) {
    try {
      Thread.sleep(interval.toMillis());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new TemplatePollingException("Template polling interrupted for " + context, e);
    }
  }

  private static void validateConfig(PollingConfig config) {
    if (config == null) {
      throw new IllegalArgumentException("PollingConfig cannot be null");
    }
    if (config.getTimeout() == null && config.getMaxAttempts() == null) {
      throw new IllegalArgumentException(
          "At least one of timeout or maxAttempts must be configured");
    }
    if (config.getPollingInterval() == null) {
      throw new IllegalArgumentException("Polling interval cannot be null");
    }
  }
}
