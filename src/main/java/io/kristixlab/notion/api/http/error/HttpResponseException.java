package io.kristixlab.notion.api.http.error;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HttpResponseException extends RuntimeException {

  private String apiName;
  private int status;
  private Object body;
  private String message;

  public HttpResponseException(String apiName, int status, Object body) {
    this(apiName, status, body, null);
  }

  public HttpResponseException(String apiName, int status, Object body, String message) {
    super(
        String.format(
            "Error in API %s: Status code %d, Message: %s, Body: %s",
            apiName, status, message != null ? message : "none", body));
    this.apiName = apiName;
    this.status = status;
    this.body = body;
    this.message = message;
  }
}
