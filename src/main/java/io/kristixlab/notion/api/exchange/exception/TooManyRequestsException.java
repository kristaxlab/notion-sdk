package io.kristixlab.notion.api.exchange.exception;

import io.kristixlab.notion.api.exchange.NotionApiException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class TooManyRequestsException extends NotionApiException {

  public TooManyRequestsException(String code, String message, String requestId) {
    super(429, code, message, requestId);
  }
}
