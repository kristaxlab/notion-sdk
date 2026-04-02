package io.kristixlab.notion.api.model;

import java.util.List;
import lombok.Data;

@Data
public class NotionError extends BaseNotionObject {

  private String error;

  private Integer status;

  private String code;

  private String message;

  private AdditionalData additionalData;

  @Data
  public static class AdditionalData {

    private String errorType;

    private String databaseId;

    private List<String> childDataSourceIds;

    private String minimumApiVersion;
  }
}
