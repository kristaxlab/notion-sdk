package io.kristixlab.notion.api.http.error;

public class GatewayTimeoutException extends NotionApiException {

  public GatewayTimeoutException(String code, String message, String requestId) {
    super(504, code, message, requestId);
  }
}
