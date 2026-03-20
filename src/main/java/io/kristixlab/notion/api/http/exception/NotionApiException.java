package io.kristixlab.notion.api.http.exception;

import lombok.Getter;

@Getter
public class NotionApiException extends RuntimeException {

  private int status;
  private String code;
  private String requestId;

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
