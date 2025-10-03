package io.kristixlab.notion.api.http.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ValidationException extends NotionApiException {

  public ValidationException(String code, String message, String requestId) {
    super(400, code, message, requestId);
  }
}
