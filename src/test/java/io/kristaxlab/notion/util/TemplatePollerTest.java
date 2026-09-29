package io.kristaxlab.notion.util;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.NotionClient;
import io.kristaxlab.notion.http.NotionHttpClient;
import io.kristaxlab.notion.http.base.request.ApiPath;
import io.kristaxlab.notion.model.block.Block;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.block.ParagraphBlock;
import io.kristaxlab.notion.model.page.Page;
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

@DisplayName("TemplatePoller")
class TemplatePollerTest {

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

  private static BlockList blockListWith(int count) {
    BlockList blocks = new BlockList();
    List<Block> results = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      results.add(new ParagraphBlock());
    }
    blocks.setResults(results);
    return blocks;
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
          () -> TemplatePoller.awaitPage(client, "page-1", page -> true, null));
    }

    @Test
    @DisplayName("rejects config without timeout or max attempts")
    void rejectsConfigWithoutLimits() {
      NotionClient client = clientWith(new QueuedNotionHttpClient());
      PollingConfig config = new PollingConfig();
      config.setPollingInterval(Duration.ofMillis(1));

      assertThrows(
          IllegalArgumentException.class,
          () -> TemplatePoller.awaitPage(client, "page-1", page -> true, config));
    }

    @Test
    @DisplayName("rejects config without polling interval")
    void rejectsConfigWithoutPollingInterval() {
      NotionClient client = clientWith(new QueuedNotionHttpClient());
      PollingConfig config = new PollingConfig();
      config.setMaxAttempts(1);

      assertThrows(
          IllegalArgumentException.class,
          () -> TemplatePoller.awaitBlocks(client, "page-1", blocks -> true, config));
    }
  }

  @Nested
  @DisplayName("awaitPage")
  class AwaitPage {

    @Test
    @DisplayName("returns immediately when page is ready on first attempt")
    void returnsImmediatelyWhenReady() {
      QueuedNotionHttpClient httpClient = new QueuedNotionHttpClient();
      Page ready = pageWithId("page-1");
      httpClient.enqueue(ready);

      NotionClient client = clientWith(httpClient);
      PollingConfig config =
          PollingConfig.builder().maxAttempts(3).pollingInterval(Duration.ofMillis(1)).build();

      Page result =
          TemplatePoller.awaitPage(client, "page-1", page -> "page-1".equals(page.getId()), config);

      assertSame(ready, result);
      assertEquals(1, httpClient.getCallCount());
    }

    @Test
    @DisplayName("polls until page becomes ready")
    void pollsUntilReady() {
      QueuedNotionHttpClient httpClient = new QueuedNotionHttpClient();
      httpClient.enqueue(pageWithId(null), pageWithId(null), pageWithId("page-1"));

      NotionClient client = clientWith(httpClient);
      PollingConfig config =
          PollingConfig.builder().maxAttempts(5).pollingInterval(Duration.ofMillis(1)).build();

      Page result =
          TemplatePoller.awaitPage(client, "page-1", page -> page.getId() != null, config);

      assertEquals("page-1", result.getId());
      assertEquals(3, httpClient.getCallCount());
    }

    @Test
    @DisplayName("throws when max attempts are exceeded")
    void throwsWhenMaxAttemptsExceeded() {
      QueuedNotionHttpClient httpClient = new QueuedNotionHttpClient();
      httpClient.enqueue(pageWithId(null), pageWithId(null), pageWithId(null));

      NotionClient client = clientWith(httpClient);
      PollingConfig config =
          PollingConfig.builder().maxAttempts(2).pollingInterval(Duration.ofMillis(1)).build();

      TemplatePollingException exception =
          assertThrows(
              TemplatePollingException.class,
              () ->
                  TemplatePoller.awaitPage(client, "page-1", page -> page.getId() != null, config));

      assertTrue(exception.getMessage().contains("exceeded max attempts (2)"));
      assertTrue(exception.getMessage().contains("page-1"));
      assertEquals(2, httpClient.getCallCount());
    }

    @Test
    @DisplayName("throws when timeout is reached")
    void throwsWhenTimeoutReached() {
      QueuedNotionHttpClient httpClient = new QueuedNotionHttpClient();
      httpClient.enqueue(pageWithId(null));

      NotionClient client = clientWith(httpClient);
      PollingConfig config =
          PollingConfig.builder()
              .timeout(Duration.ofMillis(30))
              .pollingInterval(Duration.ofMillis(15))
              .build();

      TemplatePollingException exception =
          assertThrows(
              TemplatePollingException.class,
              () ->
                  TemplatePoller.awaitPage(client, "page-1", page -> page.getId() != null, config));

      assertTrue(exception.getMessage().contains("timed out"));
      assertTrue(exception.getMessage().contains("page-1"));
      assertTrue(httpClient.getCallCount() >= 1);
    }

    @Test
    @DisplayName("throws when polling is interrupted")
    void throwsWhenInterrupted() throws Exception {
      QueuedNotionHttpClient httpClient = new QueuedNotionHttpClient();
      httpClient.enqueue(pageWithId(null));

      NotionClient client = clientWith(httpClient);
      PollingConfig config =
          PollingConfig.builder().maxAttempts(10).pollingInterval(Duration.ofSeconds(60)).build();

      Thread pollingThread = Thread.currentThread();
      Thread interrupter =
          new Thread(
              () -> {
                try {
                  Thread.sleep(50);
                  pollingThread.interrupt();
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                }
              });
      interrupter.start();

      TemplatePollingException exception =
          assertThrows(
              TemplatePollingException.class,
              () ->
                  TemplatePoller.awaitPage(client, "page-1", page -> page.getId() != null, config));

      interrupter.join();

      assertTrue(exception.getMessage().contains("interrupted"));
      assertTrue(exception.getMessage().contains("page page-1"));
      assertInstanceOf(InterruptedException.class, exception.getCause());
      assertTrue(Thread.currentThread().isInterrupted());
      Thread.interrupted();
    }
  }

  @Nested
  @DisplayName("awaitBlocks")
  class AwaitBlocks {

    @Test
    @DisplayName("returns immediately when blocks are ready on first attempt")
    void returnsImmediatelyWhenReady() {
      QueuedNotionHttpClient httpClient = new QueuedNotionHttpClient();
      BlockList ready = blockListWith(2);
      httpClient.enqueue(ready);

      NotionClient client = clientWith(httpClient);
      PollingConfig config =
          PollingConfig.builder().maxAttempts(3).pollingInterval(Duration.ofMillis(1)).build();

      BlockList result =
          TemplatePoller.awaitBlocks(
              client, "page-1", blocks -> blocks.getResults().size() == 2, config);

      assertSame(ready, result);
      assertEquals(1, httpClient.getCallCount());
    }

    @Test
    @DisplayName("polls until blocks become ready")
    void pollsUntilReady() {
      QueuedNotionHttpClient httpClient = new QueuedNotionHttpClient();
      httpClient.enqueue(blockListWith(0), blockListWith(1), blockListWith(2));

      NotionClient client = clientWith(httpClient);
      PollingConfig config =
          PollingConfig.builder().maxAttempts(5).pollingInterval(Duration.ofMillis(1)).build();

      BlockList result =
          TemplatePoller.awaitBlocks(
              client, "page-1", blocks -> blocks.getResults().size() == 2, config);

      assertEquals(2, result.getResults().size());
      assertEquals(3, httpClient.getCallCount());
    }

    @Test
    @DisplayName("throws when max attempts are exceeded")
    void throwsWhenMaxAttemptsExceeded() {
      QueuedNotionHttpClient httpClient = new QueuedNotionHttpClient();
      httpClient.enqueue(blockListWith(0), blockListWith(0));

      NotionClient client = clientWith(httpClient);
      PollingConfig config =
          PollingConfig.builder().maxAttempts(2).pollingInterval(Duration.ofMillis(1)).build();

      TemplatePollingException exception =
          assertThrows(
              TemplatePollingException.class,
              () ->
                  TemplatePoller.awaitBlocks(
                      client, "page-1", blocks -> blocks.getResults().size() >= 2, config));

      assertTrue(exception.getMessage().contains("exceeded max attempts (2)"));
      assertTrue(exception.getMessage().contains("page blocks page-1"));
      assertEquals(2, httpClient.getCallCount());
    }
  }

  @Nested
  @DisplayName("block count helpers")
  class BlockCountHelpers {

    @Test
    @DisplayName("awaitBlockCount waits for exact block count")
    void awaitBlockCount_waitsForExactCount() {
      QueuedNotionHttpClient httpClient = new QueuedNotionHttpClient();
      httpClient.enqueue(blockListWith(1), blockListWith(3));

      NotionClient client = clientWith(httpClient);
      PollingConfig config =
          PollingConfig.builder().maxAttempts(5).pollingInterval(Duration.ofMillis(1)).build();

      BlockList result = TemplatePoller.awaitBlockCount(client, "page-1", 3, config);

      assertEquals(3, result.getResults().size());
      assertEquals(2, httpClient.getCallCount());
    }

    @Test
    @DisplayName("awaitMinBlockCount waits for minimum block count")
    void awaitMinBlockCount_waitsForMinimumCount() {
      QueuedNotionHttpClient httpClient = new QueuedNotionHttpClient();
      httpClient.enqueue(blockListWith(0), blockListWith(2));

      NotionClient client = clientWith(httpClient);
      PollingConfig config =
          PollingConfig.builder().maxAttempts(5).pollingInterval(Duration.ofMillis(1)).build();

      BlockList result = TemplatePoller.awaitMinBlockCount(client, "page-1", 2, config);

      assertEquals(2, result.getResults().size());
      assertEquals(2, httpClient.getCallCount());
    }

    @Test
    @DisplayName("awaitAnyBlocks waits for at least one block")
    void awaitAnyBlocks_waitsForAtLeastOneBlock() {
      QueuedNotionHttpClient httpClient = new QueuedNotionHttpClient();
      httpClient.enqueue(blockListWith(0), blockListWith(1));

      NotionClient client = clientWith(httpClient);
      PollingConfig config =
          PollingConfig.builder().maxAttempts(5).pollingInterval(Duration.ofMillis(1)).build();

      BlockList result = TemplatePoller.awaitAnyBlocks(client, "page-1", config);

      assertEquals(1, result.getResults().size());
      assertEquals(2, httpClient.getCallCount());
    }

    @Test
    @DisplayName("awaitBlockCount treats null results as zero blocks")
    void awaitBlockCount_treatsNullResultsAsZero() {
      QueuedNotionHttpClient httpClient = new QueuedNotionHttpClient();
      BlockList empty = new BlockList();
      empty.setResults(null);
      httpClient.enqueue(empty, blockListWith(1));

      NotionClient client = clientWith(httpClient);
      PollingConfig config =
          PollingConfig.builder().maxAttempts(5).pollingInterval(Duration.ofMillis(1)).build();

      BlockList result = TemplatePoller.awaitBlockCount(client, "page-1", 1, config);

      assertEquals(1, result.getResults().size());
      assertEquals(2, httpClient.getCallCount());
    }
  }

  private static Page pageWithId(String id) {
    Page page = new Page();
    page.setId(id);
    return page;
  }

  /** Returns queued responses in order; repeats the last response when the queue is exhausted. */
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
