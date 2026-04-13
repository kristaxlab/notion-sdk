package io.kristaxlab.notion.http.error;

/** Thrown on HTTP 404 — the requested resource does not exist or is not accessible. */
public class NotFoundException extends NotionApiException {

  public NotFoundException(String code, String message, String requestId) {
    super(404, code, message, requestId);
  }
}
