package io.kristixlab.notion.api.model.blocks;

import lombok.Getter;
import lombok.Setter;

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
