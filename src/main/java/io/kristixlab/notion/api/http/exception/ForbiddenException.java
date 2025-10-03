package io.kristixlab.notion.api.http.exception;

import io.kristixlab.notion.api.http.NotionApiException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ForbiddenException extends NotionApiException {

  public ForbiddenException(String code, String message, String requestId) {
    super(403, code, message, requestId);
  }
}
