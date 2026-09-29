package io.kristaxlab.notion.util;

import io.kristaxlab.notion.NotionClient;
import io.kristaxlab.notion.model.common.NotionObject;
import io.kristaxlab.notion.model.search.SearchList;
import io.kristaxlab.notion.model.search.SearchParams;
import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;

/**
 * Utility for polling the search endpoint until an expected match appears.
 *
 * <p>Search indexing is eventually consistent: a page created or trashed moments earlier may be
 * missing from search results. This utility retries {@code search} until a readiness condition is
 * met, or until a timeout/max attempts is reached.
 *
 * <pre>{@code
 * SearchList found =
 *     SearchPoller.awaitPageId(
 *         client,
 *         SearchParams.builder().query("IT-40").filter(SearchFilter.pages()).build(),
 *         pageId,
 *         PollingConfig.ofTimeout(Duration.ofSeconds(30)));
 * }</pre>
 *
 * <p>{@link SearchPollingException} is a {@link PollingException}; catch the base type when any
 * poller failure should be handled the same way.
 *
 * @see io.kristaxlab.notion.endpoints.SearchEndpoint
 */
public final class SearchPoller {

  private SearchPoller() {}

  /**
   * Polls search until a result with the given page id appears.
   *
   * @param client Notion client for API calls
   * @param params search endpoint params sent on each attempt (not mutated)
   * @param pageId page identifier to wait for
   * @param config polling configuration (timeout, attempts, interval)
   * @return the search endpoint response that first contains the page id
   * @throws IllegalArgumentException if any argument is {@code null} or {@code pageId} is blank
   * @throws SearchPollingException if timeout, max attempts reached, or polling is interrupted
   */
  public static SearchList awaitPageId(
      NotionClient client, SearchParams params, String pageId, PollingConfig config) {
    if (pageId == null || pageId.isBlank()) {
      throw new IllegalArgumentException("pageId cannot be null or blank");
    }
    return awaitSearch(
        client, params, list -> containsObjectId(list, pageId), config, "page " + pageId);
  }

  /**
   * Polls search until a readiness condition is met.
   *
   * @param client Notion client for API calls
   * @param params search endpoint params sent on each attempt (not mutated)
   * @param readinessCheck predicate that returns true when the response is ready
   * @param config polling configuration (timeout, attempts, interval)
   * @return the search endpoint response when ready
   * @throws IllegalArgumentException if any argument is {@code null}
   * @throws SearchPollingException if timeout, max attempts reached, or polling is interrupted
   */
  public static SearchList awaitSearch(
      NotionClient client,
      SearchParams params,
      Predicate<SearchList> readinessCheck,
      PollingConfig config) {
    return awaitSearch(client, params, readinessCheck, config, "search");
  }

  private static SearchList awaitSearch(
      NotionClient client,
      SearchParams params,
      Predicate<SearchList> readinessCheck,
      PollingConfig config,
      String context) {
    if (client == null) {
      throw new IllegalArgumentException("client cannot be null");
    }
    if (params == null) {
      throw new IllegalArgumentException("params cannot be null");
    }
    if (readinessCheck == null) {
      throw new IllegalArgumentException("readinessCheck cannot be null");
    }
    validateConfig(config);

    long startTime = System.currentTimeMillis();
    Duration timeout = config.getTimeout();
    Integer maxAttempts = config.getMaxAttempts();

    SearchList lastList = null;
    int attempt = 0;

    while (true) {
      attempt++;

      if (maxAttempts != null && attempt > maxAttempts) {
        throw new SearchPollingException(
            String.format(
                "Search polling exceeded max attempts (%d) for %s", maxAttempts, context));
      }

      if (timeout != null) {
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed > timeout.toMillis()) {
          throw new SearchPollingException(
              String.format("Search polling timed out after %dms for %s", elapsed, context));
        }
      }

      lastList = client.search().search(params);
      if (readinessCheck.test(lastList)) {
        return lastList;
      }

      sleep(config.getPollingInterval(), context);
    }
  }

  private static boolean containsObjectId(SearchList list, String id) {
    List<NotionObject> results = list.getResults();
    if (results == null) {
      return false;
    }
    for (NotionObject object : results) {
      if (object != null && id.equals(object.getId())) {
        return true;
      }
    }
    return false;
  }

  private static void sleep(Duration interval, String context) {
    try {
      Thread.sleep(interval.toMillis());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new SearchPollingException("Search polling interrupted for " + context, e);
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
