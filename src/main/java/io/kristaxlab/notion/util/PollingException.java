package io.kristaxlab.notion.util;

/**
 * Base exception for poller failures (timeout, max attempts, or interrupt).
 *
 * <p>Catch this type when any poller failure should be handled the same way. Prefer a specific
 * subclass ({@link TemplatePollingException}, and later search polling) when the caller cares which
 * poller failed. New pollers should add their own subclass of this type rather than a sibling of
 * {@link RuntimeException}.
 */
public class PollingException extends RuntimeException {

  /**
   * Creates a polling exception with a detail message.
   *
   * @param message detail message
   */
  public PollingException(String message) {
    super(message);
  }

  /**
   * Creates a polling exception with a detail message and cause.
   *
   * @param message detail message
   * @param cause underlying cause
   */
  public PollingException(String message, Throwable cause) {
    super(message, cause);
  }
}
