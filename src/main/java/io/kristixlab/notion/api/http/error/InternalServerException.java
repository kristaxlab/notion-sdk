package io.kristixlab.notion.api.http.error;

/** Thrown on HTTP 500 — an unexpected error on the Notion server. */
public class InternalServerException extends NotionApiException {

  public InternalServerException(String code, String message, String requestId) {
    super(500, code, message, requestId);
  }
}
