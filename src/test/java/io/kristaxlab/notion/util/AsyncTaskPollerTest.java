package io.kristaxlab.notion.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.kristaxlab.notion.NotionClient;
import io.kristaxlab.notion.http.NotionHttpClient;
import io.kristaxlab.notion.http.base.json.JacksonSerializer;
import io.kristaxlab.notion.http.base.request.ApiPath;
import io.kristaxlab.notion.model.NotionError;
import io.kristaxlab.notion.model.asynctask.AsyncTask;
import io.kristaxlab.notion.model.asynctask.AsyncTaskStatus;
import io.kristaxlab.notion.model.page.PageAsMarkdown;
import java.lang.reflect.Constructor;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("AsyncTaskPoller")
class AsyncTaskPollerTest {

  private static NotionClient clientWith(NotionHttpClient httpClient) {
    try {
      Constructor<NotionClient> constructor =
          NotionClient.class.getDeclaredConstructor(NotionHttpClient.class);
      constructor.setAccessible(true);
      return constructor.newInstance(httpClient);
    } catch (ReflectiveOperationException e) {
      throw new IllegalStateException("Failed to construct NotionClient for test", e);
    }
  }

  @Nested
  @DisplayName("awaitTerminal")
  class AwaitTerminal {

    @Test
    @DisplayName("returns immediately when succeeded")
    void returnsImmediatelyWhenSucceeded() {
      QueuedNotionHttpClient httpClient = new QueuedNotionHttpClient();
      AsyncTask succeeded = task(AsyncTaskStatus.SUCCEEDED.getValue());
      httpClient.enqueue(succeeded);

      NotionClient client = clientWith(httpClient);
      PollingConfig config =
          PollingConfig.builder().maxAttempts(3).pollingInterval(Duration.ofMillis(1)).build();

      AsyncTask result = AsyncTaskPoller.awaitTerminal(client, "task-1", config);

      assertSame(succeeded, result);
      assertEquals(1, httpClient.getCallCount());
    }

    @Test
    @DisplayName("polls until succeeded")
    void pollsUntilSucceeded() {
      QueuedNotionHttpClient httpClient = new QueuedNotionHttpClient();
      httpClient.enqueue(
          task(AsyncTaskStatus.RUNNING.getValue()), task(AsyncTaskStatus.SUCCEEDED.getValue()));

      NotionClient client = clientWith(httpClient);
      PollingConfig config =
          PollingConfig.builder().maxAttempts(5).pollingInterval(Duration.ofMillis(1)).build();

      AsyncTask result = AsyncTaskPoller.awaitTerminal(client, "task-1", config);

      assertEquals(AsyncTaskStatus.SUCCEEDED.getValue(), result.getStatus());
      assertEquals(2, httpClient.getCallCount());
    }

    @Test
    @DisplayName("throws when task failed")
    void throwsWhenFailed() {
      QueuedNotionHttpClient httpClient = new QueuedNotionHttpClient();
      AsyncTask failed = task(AsyncTaskStatus.FAILED.getValue());
      NotionError error = new NotionError();
      error.setCode("validation_error");
      error.setMessage("bad body");
      failed.setError(error);
      httpClient.enqueue(failed);

      NotionClient client = clientWith(httpClient);
      PollingConfig config =
          PollingConfig.builder().maxAttempts(3).pollingInterval(Duration.ofMillis(1)).build();

      AsyncTaskPollingException exception =
          assertThrows(
              AsyncTaskPollingException.class,
              () -> AsyncTaskPoller.awaitTerminal(client, "task-1", config));

      assertInstanceOf(PollingException.class, exception);
      assertTrue(exception.getMessage().contains("validation_error"));
      assertTrue(exception.getMessage().contains("bad body"));
    }
  }

  @Nested
  @DisplayName("awaitPageAsMarkdown")
  class AwaitPageAsMarkdown {

    @Test
    @DisplayName("converts succeeded page_markdown result")
    void convertsPageMarkdownResult() {
      QueuedNotionHttpClient httpClient = new QueuedNotionHttpClient();
      AsyncTask succeeded = task(AsyncTaskStatus.SUCCEEDED.getValue());
      ObjectNode result = JacksonSerializer.defaultMapper().createObjectNode();
      result.put("object", "page_markdown");
      result.put("id", "page-1");
      result.put("markdown", "# Done");
      result.put("truncated", false);
      succeeded.setResult(result);
      httpClient.enqueue(succeeded);

      NotionClient client = clientWith(httpClient);
      PollingConfig config =
          PollingConfig.builder().maxAttempts(1).pollingInterval(Duration.ofMillis(1)).build();

      PageAsMarkdown page = AsyncTaskPoller.awaitPageAsMarkdown(client, "task-1", config);

      assertEquals("page-1", page.getId());
      assertEquals("# Done", page.getMarkdown());
    }
  }

  private static AsyncTask task(String status) {
    AsyncTask task = new AsyncTask();
    task.setId("task-1");
    task.setStatus(status);
    task.setPollAfterSeconds(0);
    return task;
  }

  private static final class QueuedNotionHttpClient implements NotionHttpClient {

    private final Deque<Object> responses = new ArrayDeque<>();
    private final AtomicInteger callCount = new AtomicInteger();

    void enqueue(Object... items) {
      responses.addAll(List.of(items));
    }

    int getCallCount() {
      return callCount.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T call(String method, ApiPath apiPath, Class<T> responseType) {
      callCount.incrementAndGet();
      return (T) nextResponse();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T call(String method, ApiPath apiPath, Object body, Class<T> responseType) {
      callCount.incrementAndGet();
      return (T) nextResponse();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T call(
        String method,
        ApiPath apiPath,
        Map<String, String> extraHeaders,
        Object body,
        Class<T> responseType) {
      callCount.incrementAndGet();
      return (T) nextResponse();
    }

    private Object nextResponse() {
      if (responses.isEmpty()) {
        throw new IllegalStateException("No queued HTTP response available");
      }
      if (responses.size() == 1) {
        return responses.peek();
      }
      return responses.poll();
    }
  }
}
