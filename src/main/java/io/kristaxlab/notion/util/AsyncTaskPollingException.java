package io.kristaxlab.notion.util;

/**
 * Exception thrown when async task polling times out, exceeds max attempts, is interrupted, or the
 * task reaches {@code failed}.
 *
 * <p>Extends {@link PollingException} so callers can catch either this type or the shared base for
 * any poller.
 */
public class AsyncTaskPollingException extends PollingException {

  /**
   * Creates an async task polling exception with a detail message.
   *
   * @param message detail message
   */
  public AsyncTaskPollingException(String message) {
    super(message);
  }

  /**
   * Creates an async task polling exception with a detail message and cause.
   *
   * @param message detail message
   * @param cause underlying cause
   */
  public AsyncTaskPollingException(String message, Throwable cause) {
    super(message, cause);
  }
}
