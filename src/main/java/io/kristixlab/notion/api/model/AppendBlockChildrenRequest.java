package io.kristixlab.notion.api.model;

import io.kristixlab.notion.api.model.blocks.Block;
import java.util.List;

/** Request object for appending block children. */
public class AppendBlockChildrenRequest {
  private List<Block> children;
  private String after;

  public AppendBlockChildrenRequest() {}

  public AppendBlockChildrenRequest(List<Block> children) {
    this.children = children;
  }

  public List<Block> getChildren() {
    return children;
  }

  public void setChildren(List<Block> children) {
    this.children = children;
  }

  public String getAfter() {
    return after;
  }

  public void setAfter(String after) {
    this.after = after;
  }
}
