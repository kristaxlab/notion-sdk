package io.kristaxlab.notion.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionError extends BaseNotionObject {

  private String error;

  private Integer status;

  private String code;

  private String message;

  private AdditionalData additionalData;

  @Getter
  @Setter
  public static class AdditionalData {

    private String errorType;

    private String databaseId;

    private List<String> childDataSourceIds;

    private String minimumApiVersion;
  }
}
