package io.kristixlab.notion.api.exchange;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApiExchangeException extends RuntimeException {

  private String apiName;
  private int status;
  private String body;

  public ApiExchangeException(String apiName, int status, String body) {
    super(String.format("Error in API %s: Status code %d, Body: %s", apiName, status, body));
    this.apiName = apiName;
    this.status = status;
    this.body = body;
  }
}
