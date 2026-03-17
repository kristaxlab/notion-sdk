package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.Position;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/** Request object for appending block children. */
@Data
public class AppendBlockChildrenParams {
  private List<Block> children = new ArrayList<>();
  private Position position;

  public AppendBlockChildrenParams() {}

  public AppendBlockChildrenParams(List<Block> children) {
    this.children = children;
  }

  public static AppendBlockChildrenParams of(List<Block> children) {
    AppendBlockChildrenParams params = new AppendBlockChildrenParams();
    params.setChildren(children);
    return params;
  }

  public static AppendBlockChildrenParams of(Block childBlock) {
    AppendBlockChildrenParams params = new AppendBlockChildrenParams();
    params.setChildren(new ArrayList<>());
    params.getChildren().add(childBlock);
    return params;
  }
}
