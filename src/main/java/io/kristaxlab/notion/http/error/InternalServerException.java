package io.kristaxlab.notion.http.error;

/** Thrown on HTTP 500 — an unexpected error on the Notion server. */
public class InternalServerException extends NotionApiException {

  /**
   * Creates a 500 exception mapped from Notion error payload fields.
   *
   * @param code Notion error code
   * @param message API error message
   * @param requestId request identifier from response headers/body
   */
  public InternalServerException(String code, String message, String requestId) {
    super(500, code, message, requestId);
  }
}
