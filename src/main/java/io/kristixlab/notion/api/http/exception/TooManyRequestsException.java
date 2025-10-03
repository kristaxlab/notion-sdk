package io.kristixlab.notion.api.http.exception;

import io.kristixlab.notion.api.http.NotionApiException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class TooManyRequestsException extends NotionApiException {

  public TooManyRequestsException(String code, String message, String requestId) {
    super(429, code, message, requestId);
  }
}
