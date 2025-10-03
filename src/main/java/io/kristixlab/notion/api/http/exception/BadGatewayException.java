package io.kristixlab.notion.api.http.exception;

import io.kristixlab.notion.api.http.NotionApiException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class BadGatewayException extends NotionApiException {

  public BadGatewayException(String code, String message, String requestId) {
    super(502, code, message, requestId);
  }
}
