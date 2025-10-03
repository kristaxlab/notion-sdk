package io.kristixlab.notion.api.model.blocks;

import java.util.ArrayList;
import java.util.List;

/**
 * Request object for appending block children.
 */
public class AppendBlockChildrenRequest {
  private List<Block> children = new ArrayList<>();
  private String after;

  public AppendBlockChildrenRequest() {
  }

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
