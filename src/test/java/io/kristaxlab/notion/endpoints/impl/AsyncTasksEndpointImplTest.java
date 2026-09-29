package io.kristaxlab.notion.endpoints.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.kristaxlab.notion.http.base.client.ApiClientStub;
import io.kristaxlab.notion.model.asynctask.AsyncTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Async tasks endpoint behaviors")
class AsyncTasksEndpointImplTest {

  private ApiClientStub client;
  private AsyncTasksEndpointImpl endpoint;

  @BeforeEach
  void setUp() {
    client = new ApiClientStub();
    endpoint = new AsyncTasksEndpointImpl(client);
  }

  @Nested
  @DisplayName("Retrieve async task")
  class Retrieve {

    @Test
    @DisplayName("works for valid task id")
    void retrieve_buildsGetRequest() {
      AsyncTask expected = new AsyncTask();
      client.setResponse(expected);

      AsyncTask result = endpoint.retrieve("task_abc123");

      assertEquals("GET", client.getLastMethod());
      assertEquals("/async_tasks/{task_id}", client.getLastUrlInfo().getUrl());
      assertEquals("task_abc123", client.getLastUrlInfo().getPathParams().get("task_id"));
      assertSame(expected, result);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("rejects blank or null task id")
    void retrieve_rejectsBlankOrNullTaskId(String taskId) {
      assertThrows(IllegalArgumentException.class, () -> endpoint.retrieve(taskId));
    }
  }
}
