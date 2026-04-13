package io.kristaxlab.notion.http.error;

/** Thrown on HTTP 504 — Notion's upstream server did not respond in time. */
public class GatewayTimeoutException extends NotionApiException {

  public GatewayTimeoutException(String code, String message, String requestId) {
    super(504, code, message, requestId);
  }
}
