package io.kristaxlab.notion.http.error;

/** Thrown on HTTP 403 — the token lacks permission for the requested resource. */
public class ForbiddenException extends NotionApiException {

  /**
   * Creates a 403 exception mapped from Notion error payload fields.
   *
   * @param code Notion error code
   * @param message API error message
   * @param requestId request identifier from response headers/body
   */
  public ForbiddenException(String code, String message, String requestId) {
    super(403, code, message, requestId);
  }
}
