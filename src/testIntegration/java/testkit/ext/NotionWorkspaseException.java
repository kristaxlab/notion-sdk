package testkit.ext;

/** Unchecked failure for a missing fixture page, missing test id, or missing session config. */
public class NotionWorkspaseException extends RuntimeException {
  public NotionWorkspaseException(String message) {
    super(message);
  }

  public NotionWorkspaseException(String message, Throwable cause) {
    super(message, cause);
  }
}
