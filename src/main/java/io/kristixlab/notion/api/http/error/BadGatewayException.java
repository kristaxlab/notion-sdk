package io.kristixlab.notion.api.http.error;

public class BadGatewayException extends NotionApiException {

  public BadGatewayException(String code, String message, String requestId) {
    super(502, code, message, requestId);
  }
}
