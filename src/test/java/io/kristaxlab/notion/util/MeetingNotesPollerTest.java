package io.kristaxlab.notion.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristaxlab.notion.NotionClient;
import io.kristaxlab.notion.http.NotionHttpClient;
import io.kristaxlab.notion.http.base.request.ApiPath;
import io.kristaxlab.notion.model.block.MeetingNotesBlock;
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

@DisplayName("MeetingNotesPoller")
class MeetingNotesPollerTest {

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
          () -> MeetingNotesPoller.awaitNotesReady(client, "block-1", null));
    }

    @Test
    @DisplayName("rejects blank block id")
    void rejectsBlankBlockId() {
      NotionClient client = clientWith(new QueuedNotionHttpClient());
      PollingConfig config = PollingConfig.ofAttempts(1);
      assertThrows(
          IllegalArgumentException.class,
          () -> MeetingNotesPoller.awaitNotesReady(client, "  ", config));
    }

    @Test
    @DisplayName("rejects null client")
    void rejectsNullClient() {
      PollingConfig config = PollingConfig.ofAttempts(1);
      assertThrows(
          IllegalArgumentException.class,
          () -> MeetingNotesPoller.awaitNotesReady(null, "block-1", config));
    }
  }

  @Nested
  @DisplayName("awaitNotesReady")
  class AwaitNotesReady {

    @Test
    @DisplayName("returns immediately when status is notes_ready")
    void returnsImmediatelyWhenReady() {
      QueuedNotionHttpClient httpClient = new QueuedNotionHttpClient();
      MeetingNotesBlock ready = meetingNotes("notes_ready");
      httpClient.enqueue(ready);

      NotionClient client = clientWith(httpClient);
      PollingConfig config =
          PollingConfig.builder().maxAttempts(3).pollingInterval(Duration.ofMillis(1)).build();

      MeetingNotesBlock result = MeetingNotesPoller.awaitNotesReady(client, "block-1", config);

      assertSame(ready, result);
      assertEquals(1, httpClient.getCallCount());
    }

    @Test
    @DisplayName("polls until notes_ready")
    void pollsUntilReady() {
      QueuedNotionHttpClient httpClient = new QueuedNotionHttpClient();
      httpClient.enqueue(meetingNotes("transcription_in_progress"), meetingNotes("notes_ready"));

      NotionClient client = clientWith(httpClient);
      PollingConfig config =
          PollingConfig.builder().maxAttempts(5).pollingInterval(Duration.ofMillis(1)).build();

      MeetingNotesBlock result = MeetingNotesPoller.awaitNotesReady(client, "block-1", config);

      assertEquals("notes_ready", result.getMeetingNotes().getStatus());
      assertEquals(2, httpClient.getCallCount());
    }

    @Test
    @DisplayName("throws MeetingNotesPollingException when max attempts exceeded")
    void throwsWhenMaxAttemptsExceeded() {
      QueuedNotionHttpClient httpClient = new QueuedNotionHttpClient();
      httpClient.enqueue(meetingNotes("summary_in_progress"));

      NotionClient client = clientWith(httpClient);
      PollingConfig config =
          PollingConfig.builder().maxAttempts(2).pollingInterval(Duration.ofMillis(1)).build();

      MeetingNotesPollingException exception =
          assertThrows(
              MeetingNotesPollingException.class,
              () -> MeetingNotesPoller.awaitNotesReady(client, "block-1", config));

      assertInstanceOf(PollingException.class, exception);
      assertTrue(exception.getMessage().contains("exceeded max attempts (2)"));
      assertTrue(exception.getMessage().contains("block-1"));
      assertEquals(2, httpClient.getCallCount());
    }
  }

  private static MeetingNotesBlock meetingNotes(String status) {
    MeetingNotesBlock block = new MeetingNotesBlock();
    block.setId("block-1");
    block.getMeetingNotes().setStatus(status);
    return block;
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
