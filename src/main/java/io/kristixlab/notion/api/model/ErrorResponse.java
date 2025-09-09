package io.kristixlab.notion.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse {

  @JsonProperty("object")
  private String object;

  @JsonProperty("status")
  private Integer status;

  @JsonProperty("code")
  private String code;

  @JsonProperty("message")
  private String message;

  @JsonProperty("request_id")
  private String requestId;

  @JsonProperty("additional_data")
  private AdditionalData additionalData;

  @Data
  public static class AdditionalData {
    @JsonProperty("error_type")
    private String errorType;

    @JsonProperty("database_id")
    private String databaseId;

    @JsonProperty("child_data_source_ids")
    private List<String> childDataSourceIds;

    @JsonProperty("minimum_api_version")
    private String minimumApiVersion;
  }
}
