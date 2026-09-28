package io.kristaxlab.notion.util;

/**
 * Exception thrown when template polling times out or exceeds max attempts.
 *
 * <p>Indicates that a template was not fully applied within the configured limits when using {@link
 * TemplatePoller}. Extends {@link PollingException} so callers can catch either this type or the
 * shared base for any poller.
 */
public class TemplatePollingException extends PollingException {

  /**
   * Creates a template polling exception with a detail message.
   *
   * @param message detail message
   */
  public TemplatePollingException(String message) {
    super(message);
  }

  /**
   * Creates a template polling exception with a detail message and cause.
   *
   * @param message detail message
   * @param cause underlying cause
   */
  public TemplatePollingException(String message, Throwable cause) {
    super(message, cause);
  }
}
