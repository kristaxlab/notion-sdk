package io.kristixlab.notion.api.http.transport.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HttpResponseException extends RuntimeException {

  private String apiName;
  private int status;
  private String body;
  private String message;

  public HttpResponseException(String apiName, int status, String body) {
    this(apiName, status, body, null);
  }

  public HttpResponseException(String apiName, int status, String body, String message) {
    super(String.format("Error in API %s: Status code %d, Message: %d, Body: %s",
            apiName, status, message != null ? message : "none", body));
    this.apiName = apiName;
    this.status = status;
    this.body = body;
    this.message = message;
  }
}
