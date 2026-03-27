package io.kristixlab.notion.api.http.error;

public class NotionApiException extends RuntimeException {

  private final int status;
  private final String code;
  private final String requestId;

  public NotionApiException(int status, String code, String message, String requestId) {
    super(message);
    this.status = status;
    this.code = code;
    this.requestId = requestId;
  }

  public String toString() {
    return String.format(
        "%s\n Notion API Exception - Status: %d, Code: %s, Message: %s, Request ID: %s",
        this.getClass().getName(), status, code, getMessage(), requestId);
  }

  public int getStatus() {
    return status;
  }

  public String getCode() {
    return code;
  }

  public String getRequestId() {
    return requestId;
  }
}
