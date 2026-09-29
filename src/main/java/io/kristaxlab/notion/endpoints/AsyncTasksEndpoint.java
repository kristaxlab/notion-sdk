package io.kristaxlab.notion.endpoints;

import io.kristaxlab.notion.model.asynctask.AsyncTask;

/**
 * Notion async task retrieve endpoint: poll a task accepted for background execution.
 *
 * @see <a href="https://developers.notion.com/reference/retrieve-async-task">Retrieve an async
 *     task</a>
 */
public interface AsyncTasksEndpoint {

  /**
   * Retrieves the current status (and result or error) of an async task.
   *
   * @param taskId async task identifier
   * @return the async task
   * @throws IllegalArgumentException if {@code taskId} is {@code null}, empty, or blank
   */
  AsyncTask retrieve(String taskId);
}
