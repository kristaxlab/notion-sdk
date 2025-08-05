package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Parent {

  @JsonProperty("type")
  private String type;

  @JsonProperty("block_id")
  private String blockId;

  @JsonProperty("page_id")
  private String pageId;

  @JsonProperty("database_id")
  private String databaseId;
}
