package io.kristaxlab.notion.model.block;

import io.kristaxlab.notion.model.common.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** Parameters for appending child blocks to a parent block. */
@Getter
@Setter
public class AppendBlockChildrenParams {

  private List<Block> children;
  private Position position;

  /** Creates an empty params object for deserialization and builder assembly. */
  public AppendBlockChildrenParams() {}

  /**
   * Returns a new builder for constructing params with optional position or incremental child
   * accumulation.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link AppendBlockChildrenParams}. */
  public static class Builder {
    private List<Block> children = new ArrayList<>();
    private Position position;

    private Builder() {}

    /**
     * Adds a single child block.
     *
     * @param child the block to add
     * @return this builder
     */
    public Builder children(Block child) {
      this.children.add(child);
      return this;
    }

    /**
     * Adds multiple child blocks.
     *
     * @param children the blocks to add
     * @return this builder
     */
    public Builder children(Block... children) {
      this.children.addAll(Arrays.asList(children));
      return this;
    }

    /**
     * Adds multiple child blocks.
     *
     * @param children the blocks to add
     * @return this builder
     */
    public Builder children(List<? extends Block> children) {
      this.children.addAll(children);
      return this;
    }

    /**
     * Sets the insertion position.
     *
     * @param position where to insert the children
     * @return this builder
     */
    public Builder position(Position position) {
      this.position = position;
      return this;
    }

    /**
     * Builds the params.
     *
     * @return immutable params instance
     * @throws IllegalStateException if no children were added
     */
    public AppendBlockChildrenParams build() {
      if (children.isEmpty()) {
        throw new IllegalStateException("At least one child block is required");
      }
      AppendBlockChildrenParams params = new AppendBlockChildrenParams();
      params.setChildren(new ArrayList<>(children));
      params.setPosition(position);
      return params;
    }
  }
}
