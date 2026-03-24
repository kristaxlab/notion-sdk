package io.kristixlab.notion.api.http.error;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ForbiddenException extends NotionApiException {

  public ForbiddenException(String code, String message, String requestId) {
    super(403, code, message, requestId);
  }
}
