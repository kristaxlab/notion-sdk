package io.kristixlab.notion.api.http.error;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class NotFoundException extends NotionApiException {

  public NotFoundException(String code, String message, String requestId) {
    super(404, code, message, requestId);
  }
}
