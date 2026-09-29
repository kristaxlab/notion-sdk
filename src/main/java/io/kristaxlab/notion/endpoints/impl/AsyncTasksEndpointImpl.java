package io.kristaxlab.notion.endpoints.impl;

import static io.kristaxlab.notion.endpoints.util.Validator.checkNotNullOrEmpty;

import io.kristaxlab.notion.endpoints.AsyncTasksEndpoint;
import io.kristaxlab.notion.http.base.client.ApiClient;
import io.kristaxlab.notion.http.base.request.ApiPath;
import io.kristaxlab.notion.model.asynctask.AsyncTask;

/** Provides access to Notion async task resources. */
public class AsyncTasksEndpointImpl extends BaseEndpointImpl implements AsyncTasksEndpoint {

  /**
   * Creates an async tasks endpoint backed by the provided API client.
   *
   * @param client client used to execute async task API requests
   */
  public AsyncTasksEndpointImpl(ApiClient client) {
    super(client);
  }

  @Override
  public AsyncTask retrieve(String taskId) {
    checkNotNullOrEmpty(taskId, "taskId");

    ApiPath urlInfo =
        ApiPath.builder("/async_tasks/{task_id}").pathParam("task_id", taskId).build();
    return getClient().call(GET, urlInfo, AsyncTask.class);
  }
}
