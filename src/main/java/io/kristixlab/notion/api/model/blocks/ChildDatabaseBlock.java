package io.kristixlab.notion.api.model.blocks;

import lombok.Getter;
import lombok.Setter;

/**
 * Readonly block object for child page block. Can only return in response from Notion API and is
 * not allowed in request
 */
@Getter
@Setter
public class ChildDatabaseBlock extends Block {

  private ChildDatabase childDatabase;

  public ChildDatabaseBlock() {
    setType("child_database");
    childDatabase = new ChildDatabase();
  }

  @Getter
  @Setter
  public static class ChildDatabase {

    private String title;
  }
}
