package io.kristaxlab.notion.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristaxlab.notion.NotionClient;
import io.kristaxlab.notion.http.NotionHttpClient;
import io.kristaxlab.notion.http.base.request.ApiPath;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.search.SearchList;
import io.kristaxlab.notion.model.search.SearchParams;
import java.lang.reflect.Constructor;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("SearchPoller")
class SearchPollerTest {

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
  @DisplayName("config validation")
  class ConfigValidation {

    @Test
    @DisplayName("rejects null config")
    void rejectsNullConfig() {
      NotionClient client = clientWith(new QueuedNotionHttpClient());
      assertThrows(
          IllegalArgumentException.class,
          () -> SearchPoller.awaitPageId(client, new SearchParams(), "page-1", null));
    }

    @Test
    @DisplayName("rejects blank page id")
    void rejectsBlankPageId() {
      NotionClient client = clientWith(new QueuedNotionHttpClient());
      PollingConfig config = PollingConfig.ofAttempts(1);
      assertThrows(
          IllegalArgumentException.class,
          () -> SearchPoller.awaitPageId(client, new SearchParams(), "  ", config));
    }

    @Test
    @DisplayName("rejects null params")
    void rejectsNullParams() {
      NotionClient client = clientWith(new QueuedNotionHttpClient());
      PollingConfig config = PollingConfig.ofAttempts(1);
      assertThrows(
          IllegalArgumentException.class,
          () -> SearchPoller.awaitPageId(client, null, "page-1", config));
    }
  }

  @Nested
  @DisplayName("awaitPageId")
  class AwaitPageId {

    @Test
    @DisplayName("returns immediately when page id is present")
    void returnsImmediatelyWhenReady() {
      QueuedNotionHttpClient httpClient = new QueuedNotionHttpClient();
      SearchList ready = searchListWithPage("page-1");
      httpClient.enqueue(ready);

      NotionClient client = clientWith(httpClient);
      PollingConfig config =
          PollingConfig.builder().maxAttempts(3).pollingInterval(Duration.ofMillis(1)).build();

      SearchList result =
          SearchPoller.awaitPageId(
              client, SearchParams.builder().query("x").build(), "page-1", config);

      assertSame(ready, result);
      assertEquals(1, httpClient.getCallCount());
    }

    @Test
    @DisplayName("polls until page id appears")
    void pollsUntilReady() {
      QueuedNotionHttpClient httpClient = new QueuedNotionHttpClient();
      httpClient.enqueue(searchListWithPage("other"), searchListWithPage("page-1"));

      NotionClient client = clientWith(httpClient);
      PollingConfig config =
          PollingConfig.builder().maxAttempts(5).pollingInterval(Duration.ofMillis(1)).build();

      SearchList result = SearchPoller.awaitPageId(client, new SearchParams(), "page-1", config);

      assertEquals("page-1", result.getResults().get(0).getId());
      assertEquals(2, httpClient.getCallCount());
    }

    @Test
    @DisplayName("throws SearchPollingException when max attempts exceeded")
    void throwsWhenMaxAttemptsExceeded() {
      QueuedNotionHttpClient httpClient = new QueuedNotionHttpClient();
      httpClient.enqueue(searchListWithPage("other"));

      NotionClient client = clientWith(httpClient);
      PollingConfig config =
          PollingConfig.builder().maxAttempts(2).pollingInterval(Duration.ofMillis(1)).build();

      SearchPollingException exception =
          assertThrows(
              SearchPollingException.class,
              () -> SearchPoller.awaitPageId(client, new SearchParams(), "page-1", config));

      assertInstanceOf(PollingException.class, exception);
      assertTrue(exception.getMessage().contains("exceeded max attempts (2)"));
      assertTrue(exception.getMessage().contains("page-1"));
      assertEquals(2, httpClient.getCallCount());
    }
  }

  private static SearchList searchListWithPage(String pageId) {
    SearchList list = new SearchList();
    Page page = new Page();
    page.setId(pageId);
    List<io.kristaxlab.notion.model.common.NotionObject> results = new ArrayList<>();
    results.add(page);
    list.setResults(results);
    return list;
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
