package io.kristaxlab.notion.http.error;

/** Thrown on HTTP 502 — Notion received an invalid response from an upstream server. */
public class BadGatewayException extends NotionApiException {

  /**
   * Creates a 502 exception mapped from Notion error payload fields.
   *
   * @param code Notion error code
   * @param message API error message
   * @param requestId request identifier from response headers/body
   */
  public BadGatewayException(String code, String message, String requestId) {
    super(502, code, message, requestId);
  }
}
