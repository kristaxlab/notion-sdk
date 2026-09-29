package io.kristaxlab.notion.model.asynctask;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.kristaxlab.notion.http.base.json.JacksonSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AsyncTask deserialization")
class AsyncTaskDeserializationTest {

  @Test
  @DisplayName("deserializes queued async task example")
  void deserializesQueuedTask() {
    String json =
        """
        {
          "object": "async_task",
          "id": "task_abc123",
          "status": "queued",
          "status_url": "https://api.notion.com/v1/async_tasks/task_abc123",
          "created_time": "2026-06-29T12:00:00.000Z",
          "poll_after_seconds": 2,
          "operation": {
            "surface": "rest",
            "name": "POST /v1/pages"
          }
        }
        """;

    AsyncTask task = JacksonSerializer.withDefaults().toObject(json, AsyncTask.class);

    assertEquals("async_task", task.getObject());
    assertEquals("task_abc123", task.getId());
    assertEquals("queued", task.getStatus());
    assertEquals("https://api.notion.com/v1/async_tasks/task_abc123", task.getStatusUrl());
    assertEquals(2, task.getPollAfterSeconds());
    assertNotNull(task.getOperation());
    assertEquals("rest", task.getOperation().getSurface());
    assertEquals("POST /v1/pages", task.getOperation().getName());
  }

  @Test
  @DisplayName("deserializes succeeded task with page_markdown result")
  void deserializesSucceededWithResult() {
    String json =
        """
        {
          "object": "async_task",
          "id": "task_abc123",
          "status": "succeeded",
          "status_url": "https://api.notion.com/v1/async_tasks/task_abc123",
          "created_time": "2026-06-29T12:00:00.000Z",
          "operation": {
            "surface": "rest",
            "name": "PATCH /v1/pages/:page_id/markdown"
          },
          "result": {
            "object": "page_markdown",
            "id": "page-uuid",
            "markdown": "# Updated",
            "truncated": false,
            "unknown_block_ids": []
          }
        }
        """;

    AsyncTask task = JacksonSerializer.withDefaults().toObject(json, AsyncTask.class);

    assertEquals("succeeded", task.getStatus());
    assertNotNull(task.getResult());
    assertEquals("page_markdown", task.getResult().path("object").asText());
    assertEquals("page-uuid", task.getResult().path("id").asText());
  }
}
