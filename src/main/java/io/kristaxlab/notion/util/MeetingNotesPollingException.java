package io.kristaxlab.notion.util;

/**
 * Exception thrown when meeting-notes polling times out, exceeds max attempts, or is interrupted.
 *
 * <p>Indicates that the meeting notes block did not reach the expected status within the configured
 * limits when using {@link MeetingNotesPoller}. Extends {@link PollingException} so callers can
 * catch either this type or the shared base for any poller.
 */
public class MeetingNotesPollingException extends PollingException {

  /**
   * Creates a meeting-notes polling exception with a detail message.
   *
   * @param message detail message
   */
  public MeetingNotesPollingException(String message) {
    super(message);
  }

  /**
   * Creates a meeting-notes polling exception with a detail message and cause.
   *
   * @param message detail message
   * @param cause underlying cause
   */
  public MeetingNotesPollingException(String message, Throwable cause) {
    super(message, cause);
  }
}
