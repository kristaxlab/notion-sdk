package io.kristaxlab.notion.model.asynctask;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AsyncTaskStatus")
class AsyncTaskStatusTest {

  @Test
  @DisplayName("getValue returns wire tokens")
  void getValue_returnsWireTokens() {
    assertEquals("queued", AsyncTaskStatus.QUEUED.getValue());
    assertEquals("running", AsyncTaskStatus.RUNNING.getValue());
    assertEquals("retrying", AsyncTaskStatus.RETRYING.getValue());
    assertEquals("succeeded", AsyncTaskStatus.SUCCEEDED.getValue());
    assertEquals("failed", AsyncTaskStatus.FAILED.getValue());
  }

  @Test
  @DisplayName("matches compares against wire token")
  void matches_comparesWireToken() {
    assertTrue(AsyncTaskStatus.SUCCEEDED.matches("succeeded"));
    assertFalse(AsyncTaskStatus.SUCCEEDED.matches("failed"));
    assertFalse(AsyncTaskStatus.SUCCEEDED.matches(null));
  }
}
