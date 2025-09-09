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

  @JsonProperty("data_source_id")
  private String datasourceId;

  @JsonProperty("workspace")
  private Boolean workspace;

  public static Parent workspaceParent() {
    Parent parent = new Parent();
    parent.setType("workspace");
    parent.setWorkspace(true);
    return parent;
  }

  public static Parent pageParent(String pageId) {
    Parent parent = new Parent();
    parent.setType("page_id");
    parent.setPageId(pageId);
    return parent;
  }

  public static Parent databaseParent(String databaseId) {
    Parent parent = new Parent();
    parent.setType("database_id");
    parent.setDatabaseId(databaseId);
    return parent;
  }

  public static Parent datasourceParent(String dataSourceId) {
    Parent parent = new Parent();
    parent.setType("data_source_id");
    parent.setDatasourceId(dataSourceId);
    return parent;
  }

  public static Parent blockParent(String blockId) {
    Parent parent = new Parent();
    parent.setType("block_id");
    parent.setBlockId(blockId);
    return parent;
  }
}
