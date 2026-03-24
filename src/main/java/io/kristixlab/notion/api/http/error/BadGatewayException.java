package io.kristixlab.notion.api.http.error;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class BadGatewayException extends NotionApiException {

  public BadGatewayException(String code, String message, String requestId) {
    super(502, code, message, requestId);
  }
}
