package io.kristaxlab.notion.http.error;

import lombok.Getter;

/**
 * Base exception for all Notion API errors. Carries the HTTP status, Notion error code, and request
 * ID for diagnostics.
 */
public class NotionApiException extends RuntimeException {
  @Getter private final int status;
  @Getter private final String code;
  @Getter private final String requestId;

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
}
