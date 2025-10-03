package io.kristixlab.notion.api.http.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ServiceUnavailableException extends NotionApiException {

  public ServiceUnavailableException(String code, String message, String requestId) {
    super(503, code, message, requestId);
  }
}
