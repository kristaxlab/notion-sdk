package io.kristixlab.notion.api.exchange.exception;

import io.kristixlab.notion.api.exchange.NotionApiException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class NotFoundException extends NotionApiException {

  public NotFoundException(String code, String message, String requestId) {
    super(404, code, message, requestId);
  }
}
