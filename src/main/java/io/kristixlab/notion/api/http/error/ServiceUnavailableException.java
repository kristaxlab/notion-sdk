package io.kristixlab.notion.api.http.error;

/** Thrown on HTTP 503 — Notion is temporarily unavailable (maintenance or overload). */
public class ServiceUnavailableException extends NotionApiException {

  public ServiceUnavailableException(String code, String message, String requestId) {
    super(503, code, message, requestId);
  }
}
