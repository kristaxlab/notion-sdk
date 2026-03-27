package io.kristixlab.notion.api.http.error;

/** Thrown on HTTP 502 — Notion received an invalid response from an upstream server. */
public class BadGatewayException extends NotionApiException {

  public BadGatewayException(String code, String message, String requestId) {
    super(502, code, message, requestId);
  }
}
