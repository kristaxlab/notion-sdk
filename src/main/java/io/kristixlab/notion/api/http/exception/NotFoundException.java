package io.kristixlab.notion.api.http.exception;

import io.kristixlab.notion.api.http.NotionApiException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class NotFoundException extends NotionApiException {

  public NotFoundException(String code, String message, String requestId) {
    super(404, code, message, requestId);
  }
}
