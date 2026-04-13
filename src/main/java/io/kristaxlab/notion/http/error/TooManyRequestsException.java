package io.kristaxlab.notion.http.error;

/** Thrown on HTTP 429 — rate limit exceeded; retry after the period indicated by Notion. */
public class TooManyRequestsException extends NotionApiException {

  public TooManyRequestsException(String code, String message, String requestId) {
    super(429, code, message, requestId);
  }
}
