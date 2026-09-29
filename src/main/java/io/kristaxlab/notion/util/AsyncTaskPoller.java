package io.kristaxlab.notion.util;

import com.fasterxml.jackson.databind.JsonNode;
import io.kristaxlab.notion.NotionClient;
import io.kristaxlab.notion.http.base.json.JacksonSerializer;
import io.kristaxlab.notion.model.NotionError;
import io.kristaxlab.notion.model.asynctask.AsyncTask;
import io.kristaxlab.notion.model.asynctask.AsyncTaskStatus;
import io.kristaxlab.notion.model.page.PageAsMarkdown;
import java.time.Duration;

/**
 * Utility for polling an async task until it reaches a terminal status.
 *
 * <p>Use after {@link io.kristaxlab.notion.endpoints.PagesEndpoint#createAsync} or {@link
 * io.kristaxlab.notion.endpoints.PagesEndpoint#updateAsMarkdownAsync} returns an async task.
 *
 * <pre>{@code
 * AsyncTask started =
 *     client.pages().createAsync(p -> p.inPage(parentId).markdown("# Large body..."));
 * AsyncTask done =
 *     AsyncTaskPoller.awaitTerminal(
 *         client, started.getId(), PollingConfig.ofTimeout(Duration.ofMinutes(5)));
 * }</pre>
 *
 * <p>For markdown updates whose succeeded {@code result} is page-as-markdown, prefer {@link
 * #awaitPageAsMarkdown}.
 *
 * <p>{@link AsyncTaskPollingException} is a {@link PollingException}; catch the base type when any
 * poller failure should be handled the same way.
 */
public final class AsyncTaskPoller {

  private AsyncTaskPoller() {}

  /**
   * Polls until the async task is {@code succeeded} or {@code failed}.
   *
   * <p>On {@code failed}, throws {@link AsyncTaskPollingException}. On {@code succeeded}, returns
   * the task (callers inspect {@code result}).
   *
   * @param client Notion client for API calls
   * @param taskId async task identifier
   * @param config polling configuration (timeout, attempts, interval)
   * @return the succeeded async task
   * @throws IllegalArgumentException if any argument is invalid
   * @throws AsyncTaskPollingException if the task fails, polling times out, max attempts are
   *     exceeded, or polling is interrupted
   */
  public static AsyncTask awaitTerminal(NotionClient client, String taskId, PollingConfig config) {
    if (client == null) {
      throw new IllegalArgumentException("client cannot be null");
    }
    if (taskId == null || taskId.isBlank()) {
      throw new IllegalArgumentException("taskId cannot be null or blank");
    }
    validateConfig(config);

    long startTime = System.currentTimeMillis();
    Duration timeout = config.getTimeout();
    Integer maxAttempts = config.getMaxAttempts();

    AsyncTask last = null;
    int attempt = 0;

    while (true) {
      attempt++;

      if (maxAttempts != null && attempt > maxAttempts) {
        throw new AsyncTaskPollingException(
            String.format(
                "Async task polling exceeded max attempts (%d) for task %s", maxAttempts, taskId));
      }

      if (timeout != null) {
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed > timeout.toMillis()) {
          throw new AsyncTaskPollingException(
              String.format(
                  "Async task polling timed out after %dms for task %s", elapsed, taskId));
        }
      }

      last = client.asyncTasks().retrieve(taskId);
      String status = last.getStatus();
      if (AsyncTaskStatus.SUCCEEDED.getValue().equals(status)) {
        return last;
      }
      if (AsyncTaskStatus.FAILED.getValue().equals(status)) {
        throw new AsyncTaskPollingException(failedMessage(taskId, last.getError()));
      }

      sleep(delayFor(last, config), taskId);
    }
  }

  /**
   * Polls until the async task succeeds and converts {@code result} to {@link PageAsMarkdown}.
   *
   * <p>Use for markdown update tasks whose succeeded result is a page-as-markdown payload.
   *
   * @param client Notion client for API calls
   * @param taskId async task identifier
   * @param config polling configuration (timeout, attempts, interval)
   * @return the page-as-markdown result
   * @throws IllegalArgumentException if any argument is invalid
   * @throws AsyncTaskPollingException if polling fails or {@code result} is missing or not
   *     page-as-markdown
   */
  public static PageAsMarkdown awaitPageAsMarkdown(
      NotionClient client, String taskId, PollingConfig config) {
    AsyncTask task = awaitTerminal(client, taskId, config);
    JsonNode result = task.getResult();
    if (result == null || result.isNull()) {
      throw new AsyncTaskPollingException(
          "Async task " + taskId + " succeeded without a result payload");
    }
    String object = result.path("object").asText(null);
    if (object != null && !"page_markdown".equals(object)) {
      throw new AsyncTaskPollingException(
          "Async task "
              + taskId
              + " succeeded with object '"
              + object
              + "', expected page_markdown");
    }
    try {
      return JacksonSerializer.defaultMapper().treeToValue(result, PageAsMarkdown.class);
    } catch (Exception e) {
      throw new AsyncTaskPollingException(
          "Failed to convert async task " + taskId + " result to PageAsMarkdown", e);
    }
  }

  private static Duration delayFor(AsyncTask task, PollingConfig config) {
    Duration configured = config.getPollingInterval();
    Integer pollAfterSeconds = task.getPollAfterSeconds();
    if (pollAfterSeconds == null || pollAfterSeconds <= 0) {
      return configured;
    }
    Duration fromTask = Duration.ofSeconds(pollAfterSeconds);
    return fromTask.compareTo(configured) > 0 ? fromTask : configured;
  }

  private static String failedMessage(String taskId, NotionError error) {
    if (error == null) {
      return "Async task " + taskId + " failed";
    }
    String code = error.getCode() != null ? error.getCode() : "unknown";
    String message = error.getMessage() != null ? error.getMessage() : "";
    return String.format("Async task %s failed (%s): %s", taskId, code, message);
  }

  private static void sleep(Duration interval, String taskId) {
    try {
      Thread.sleep(interval.toMillis());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new AsyncTaskPollingException("Async task polling interrupted for task " + taskId, e);
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
