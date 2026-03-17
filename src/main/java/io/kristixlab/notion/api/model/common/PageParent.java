package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PageParent {

  @JsonProperty("type")
  private String type;

  @JsonProperty("page_id")
  private String pageId;

  @JsonProperty("database_id")
  private String databaseId;

  @JsonProperty("data_source_id")
  private String datasourceId;

  @JsonProperty("workspace")
  private Boolean workspace;

  public static PageParent workspaceParent() {
    PageParent parent = new PageParent();
    parent.setType("workspace");
    parent.setWorkspace(true);
    return parent;
  }

  public static PageParent pageParent(String pageId) {
    PageParent parent = new PageParent();
    parent.setType("page_id");
    parent.setPageId(pageId);
    return parent;
  }

  public static PageParent databaseParent(String databaseId) {
    PageParent parent = new PageParent();
    parent.setType("database_id");
    parent.setDatabaseId(databaseId);
    return parent;
  }

  public static PageParent datasourceParent(String dataSourceId) {
    PageParent parent = new PageParent();
    parent.setType("data_source_id");
    parent.setDatasourceId(dataSourceId);
    return parent;
  }
}
