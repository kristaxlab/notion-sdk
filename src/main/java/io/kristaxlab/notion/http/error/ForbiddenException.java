package io.kristaxlab.notion.http.error;

/** Thrown on HTTP 403 — the token lacks permission for the requested resource. */
public class ForbiddenException extends NotionApiException {

  public ForbiddenException(String code, String message, String requestId) {
    super(403, code, message, requestId);
  }
}
