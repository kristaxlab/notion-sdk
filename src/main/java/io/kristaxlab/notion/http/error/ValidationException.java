package io.kristaxlab.notion.http.error;

/** Thrown on HTTP 400 — the request body or parameters failed Notion's validation. */
public class ValidationException extends NotionApiException {

  public ValidationException(String code, String message, String requestId) {
    super(400, code, message, requestId);
  }
}
