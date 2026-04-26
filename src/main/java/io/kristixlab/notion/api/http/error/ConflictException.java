package io.kristixlab.notion.api.http.error;

/** Thrown on HTTP 409 — a conflict with the current state of the resource. */
public class ConflictException extends NotionApiException {

  public ConflictException(String code, String message, String requestId) {
    super(409, code, message, requestId);
  }
}
