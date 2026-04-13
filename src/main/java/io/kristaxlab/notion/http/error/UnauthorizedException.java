package io.kristaxlab.notion.http.error;

/** Thrown on HTTP 401 — the API token is missing or invalid. */
public class UnauthorizedException extends NotionApiException {

  public UnauthorizedException(String code, String message, String requestId) {
    super(401, code, message, requestId);
  }
}
