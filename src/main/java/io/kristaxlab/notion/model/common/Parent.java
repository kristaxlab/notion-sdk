package io.kristaxlab.notion.model.common;

import lombok.Getter;
import lombok.Setter;

/**
 * Parent container for a page or block: workspace, page, block, database, or data source. Prefer
 * the static factories when building requests; only set fields directly if you need full control.
 */
@Getter
@Setter
public class Parent {

  private String type;

  private String blockId;

  private String pageId;

  private String databaseId;

  private String dataSourceId;

  private Boolean workspace;

  /** Top-level workspace parent ({@code type: workspace}). */
  public static Parent workspaceParent() {
    Parent parent = new Parent();
    parent.setType("workspace");
    parent.setWorkspace(true);
    return parent;
  }

  /** Parent is an existing page ({@code type: page_id}). */
  public static Parent pageParent(String pageId) {
    Parent parent = new Parent();
    parent.setType("page_id");
    parent.setPageId(pageId);
    return parent;
  }

  /** Parent is a database ({@code type: database_id}). */
  public static Parent databaseParent(String databaseId) {
    Parent parent = new Parent();
    parent.setType("database_id");
    parent.setDatabaseId(databaseId);
    return parent;
  }

  /** Parent is a data source ({@code type: data_source_id}). */
  public static Parent dataSourceParent(String dataSourceId) {
    Parent parent = new Parent();
    parent.setType("data_source_id");
    parent.setDataSourceId(dataSourceId);
    return parent;
  }

  /** Parent is a block ({@code type: block_id}). */
  public static Parent blockParent(String blockId) {
    Parent parent = new Parent();
    parent.setType("block_id");
    parent.setBlockId(blockId);
    return parent;
  }
}
