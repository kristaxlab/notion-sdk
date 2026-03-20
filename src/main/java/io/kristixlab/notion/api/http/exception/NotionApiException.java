package io.kristixlab.notion.api.http.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NotionApiException extends RuntimeException {

  private int status;
  private String code;
  private String request_id;

  public NotionApiException(int status, String code, String message, String request_id) {
    super(message);
    this.status = status;
    this.code = code;
    this.request_id = request_id;
  }

  public String toString() {
    return String.format(
        "%s\n Notion API Exception - Status: %d, Code: %s, Message: %s, Request ID: %s",
        this.getClass().getName(), status, code, getMessage(), request_id);
  }
}
