package io.kristixlab.notion.api.http.error;

public class InternalServerException extends NotionApiException {

  public InternalServerException(String code, String message, String requestId) {
    super(500, code, message, requestId);
  }
}
