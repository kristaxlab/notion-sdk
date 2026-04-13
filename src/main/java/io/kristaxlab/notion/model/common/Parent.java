package io.kristaxlab.notion.model.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Parent {

  private String type;

  private String blockId;

  private String pageId;

  private String databaseId;

  private String dataSourceId;

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

  public static Parent dataSourceParent(String dataSourceId) {
    Parent parent = new Parent();
    parent.setType("data_source_id");
    parent.setDataSourceId(dataSourceId);
    return parent;
  }

  public static Parent blockParent(String blockId) {
    Parent parent = new Parent();
    parent.setType("block_id");
    parent.setBlockId(blockId);
    return parent;
  }
}
