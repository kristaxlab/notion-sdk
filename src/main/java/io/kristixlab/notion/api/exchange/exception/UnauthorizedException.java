package io.kristixlab.notion.api.exchange.exception;

import io.kristixlab.notion.api.exchange.NotionApiException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class UnauthorizedException extends NotionApiException {

  public UnauthorizedException(String code, String message, String requestId) {
    super(401, code, message, requestId);
  }
}
