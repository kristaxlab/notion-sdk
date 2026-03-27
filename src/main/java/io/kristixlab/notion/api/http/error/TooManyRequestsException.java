package io.kristixlab.notion.api.http.error;

public class TooManyRequestsException extends NotionApiException {

  public TooManyRequestsException(String code, String message, String requestId) {
    super(429, code, message, requestId);
  }
}
