package io.kristaxlab.notion.util;

/**
 * Exception thrown when search polling times out, exceeds max attempts, or is interrupted.
 *
 * <p>Indicates that the expected page (or other match) did not appear in search endpoint responses
 * within the configured limits when using {@link SearchPoller}. Extends {@link PollingException} so
 * callers can catch either this type or the shared base for any poller.
 */
public class SearchPollingException extends PollingException {

  /**
   * Creates a search polling exception with a detail message.
   *
   * @param message detail message
   */
  public SearchPollingException(String message) {
    super(message);
  }

  /**
   * Creates a search polling exception with a detail message and cause.
   *
   * @param message detail message
   * @param cause underlying cause
   */
  public SearchPollingException(String message, Throwable cause) {
    super(message, cause);
  }
}
