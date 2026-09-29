package io.kristaxlab.notion.model.asynctask;

/**
 * Known status tokens for an async task that the SDK can reason about today.
 *
 * <p>{@link AsyncTask} keeps {@code status} as a {@link String} so unknown Notion tokens still
 * deserialize. Use these constants when comparing a retrieved status:
 *
 * <pre>{@code
 * if (AsyncTaskStatus.SUCCEEDED.matches(task.getStatus())) { ... }
 * if (AsyncTaskStatus.FAILED.getValue().equals(task.getStatus())) { ... }
 * }</pre>
 *
 * @see AsyncTask
 */
public enum AsyncTaskStatus {
  /** Task accepted and persisted; processing has not started. */
  QUEUED("queued"),

  /** A worker is processing the task. */
  RUNNING("running"),

  /** Retryable failure; Notion will retry. */
  RETRYING("retrying"),

  /** Completed successfully; {@code result} is present. */
  SUCCEEDED("succeeded"),

  /** Failed terminally; {@code error} is present. */
  FAILED("failed");

  private final String value;

  AsyncTaskStatus(String value) {
    this.value = value;
  }

  /**
   * Returns the Notion wire token.
   *
   * @return status token
   */
  public String getValue() {
    return value;
  }

  /**
   * Returns whether {@code status} equals this constant's wire token.
   *
   * @param status status string from an {@link AsyncTask}, may be {@code null}
   * @return {@code true} when the strings are equal
   */
  public boolean matches(String status) {
    return value.equals(status);
  }
}
