package testkit.ext;

public class NotionWorkspaseException extends RuntimeException {
  public NotionWorkspaseException(String message) {
    super(message);
  }

  public NotionWorkspaseException(String message, Throwable cause) {
    super(message, cause);
  }
}
