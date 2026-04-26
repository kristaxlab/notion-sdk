package io.kristaxlab.notion.http.error;

/** Thrown on HTTP 404 — the requested resource does not exist or is not accessible. */
public class NotFoundException extends NotionApiException {

  /**
   * Creates a 404 exception mapped from Notion error payload fields.
   *
   * @param code Notion error code
   * @param message API error message
   * @param requestId request identifier from response headers/body
   */
  public NotFoundException(String code, String message, String requestId) {
    super(404, code, message, requestId);
  }
}
