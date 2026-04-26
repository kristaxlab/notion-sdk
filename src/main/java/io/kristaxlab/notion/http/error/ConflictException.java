package io.kristaxlab.notion.http.error;

/** Thrown on HTTP 409 — a conflict with the current state of the resource. */
public class ConflictException extends NotionApiException {

  /**
   * Creates a 409 exception mapped from Notion error payload fields.
   *
   * @param code Notion error code
   * @param message API error message
   * @param requestId request identifier from response headers/body
   */
  public ConflictException(String code, String message, String requestId) {
    super(409, code, message, requestId);
  }
}
