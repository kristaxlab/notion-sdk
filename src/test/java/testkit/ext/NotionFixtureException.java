package testkit.ext;

public class NotionFixtureException extends RuntimeException {
  public NotionFixtureException(String message) {
    super(message);
  }

  public NotionFixtureException(String message, Throwable cause) {
    super(message, cause);
  }
}
