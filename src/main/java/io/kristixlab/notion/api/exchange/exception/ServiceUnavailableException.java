package io.kristixlab.notion.api.exchange.exception;

import io.kristixlab.notion.api.exchange.NotionApiException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ServiceUnavailableException extends NotionApiException {

  public ServiceUnavailableException(String code, String message, String requestId) {
    super(503, code, message, requestId);
  }
}
