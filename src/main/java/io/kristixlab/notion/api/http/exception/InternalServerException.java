package io.kristixlab.notion.api.http.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class InternalServerException extends NotionApiException {

  public InternalServerException(String code, String message, String requestId) {
    super(500, code, message, requestId);
  }
}
