package io.kristaxlab.notion.model.asynctask;

import com.fasterxml.jackson.databind.JsonNode;
import io.kristaxlab.notion.model.BaseNotionObject;
import io.kristaxlab.notion.model.NotionError;
import lombok.Getter;
import lombok.Setter;

/**
 * Handle for a Notion write accepted for background execution ({@code "object": "async_task"}).
 *
 * <p>Returned on HTTP 202 when {@code allow_async} is set on a supported markdown create or update,
 * and by the async task retrieve endpoint while polling.
 *
 * @see io.kristaxlab.notion.endpoints.AsyncTasksEndpoint
 * @see io.kristaxlab.notion.util.AsyncTaskPoller
 */
@Getter
@Setter
public class AsyncTask extends BaseNotionObject {

  private String id;

  /** Wire status token; see {@link AsyncTaskStatus} for values the SDK knows today. */
  private String status;

  private String statusUrl;

  private String createdTime;

  private Integer pollAfterSeconds;

  private AsyncTaskOperation operation;

  /**
   * Present when {@code status} is {@code succeeded}. Shape depends on the originating operation
   * (for markdown update, typically a page-as-markdown payload).
   */
  private JsonNode result;

  /** Present when {@code status} is {@code failed}. */
  private NotionError error;
}
