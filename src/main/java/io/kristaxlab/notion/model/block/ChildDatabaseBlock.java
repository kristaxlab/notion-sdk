package io.kristaxlab.notion.model.block;

import lombok.Getter;
import lombok.Setter;

/**
 * A read-only Notion child database block. Returned by the API to represent a nested database
 * reference; cannot be created via the API.
 */
@Getter
@Setter
public class ChildDatabaseBlock extends Block {

  private ChildDatabase childDatabase;

  public ChildDatabaseBlock() {
    setType(BlockType.CHILD_DATABASE.getValue());
    childDatabase = new ChildDatabase();
  }

  @Getter
  @Setter
  public static class ChildDatabase {

    private String title;
  }
}
