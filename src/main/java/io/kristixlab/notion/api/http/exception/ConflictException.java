package io.kristixlab.notion.api.http.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ConflictException extends NotionApiException {

  public ConflictException(String code, String message, String requestId) {
    super(409, code, message, requestId);
  }
}
