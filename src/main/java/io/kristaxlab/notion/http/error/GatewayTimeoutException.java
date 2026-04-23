package io.kristaxlab.notion.http.error;

/** Thrown on HTTP 504 — Notion's upstream server did not respond in time. */
public class GatewayTimeoutException extends NotionApiException {

  /**
   * Creates a 504 exception mapped from Notion error payload fields.
   *
   * @param code Notion error code
   * @param message API error message
   * @param requestId request identifier from response headers/body
   */
  public GatewayTimeoutException(String code, String message, String requestId) {
    super(504, code, message, requestId);
  }
}
