package io.kristixlab.notion.api.http.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NotionApiException extends RuntimeException {

  private int status;
  private String code;
  private String message;
  private String request_id;

  public String toString() {
    return String.format(
        "%s\n Notion API Exception - Status: %d, Code: %s, Message: %s, Request ID: %s",
        this.getClass().getName(), status, code, message, request_id);
  }
}
