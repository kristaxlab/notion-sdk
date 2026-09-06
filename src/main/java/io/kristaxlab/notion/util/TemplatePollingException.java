package io.kristaxlab.notion.util;

/**
 * Exception thrown when template polling times out or exceeds max attempts.
 *
 * <p>Indicates that a template was not fully applied within the configured limits when using {@link
 * TemplatePoller}.
 */
public class TemplatePollingException extends RuntimeException {

  public TemplatePollingException(String message) {
    super(message);
  }

  public TemplatePollingException(String message, Throwable cause) {
    super(message, cause);
  }
}
