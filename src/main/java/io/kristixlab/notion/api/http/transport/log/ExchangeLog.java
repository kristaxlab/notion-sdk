package io.kristixlab.notion.api.http.transport.log;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Data;

@Data
public class ExchangeLog {

  @JsonProperty("method")
  private String method;

  @JsonProperty("path")
  private String path;

  @JsonProperty("headers")
  private Map headers;

  @JsonProperty("type")
  private String type;

  @JsonProperty("request_body")
  private Object requestBody;

  @JsonProperty("status_code")
  private Integer statusCode;

  @JsonProperty("response_body")
  private Object responseBody;
}
