package io.kristixlab.notion.api.model.block;

import io.kristixlab.notion.api.model.common.Position;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Parameters for appending child blocks to a parent block.
 *
 * <p>Use the static {@code of(...)} factories for simple cases, or {@code builder()} when you need
 * to set a position or accumulate children incrementally:
 *
 * <pre>{@code
 * // Simple — single block
 * AppendBlockChildrenParams.of(ParagraphBlock.of("Hello"))
 *
 * // Simple — multiple blocks
 * AppendBlockChildrenParams.of(
 *     ParagraphBlock.of("Line 1"),
 *     ParagraphBlock.of("Line 2")
 * )
 *
 * // With position — use the builder
 * AppendBlockChildrenParams.builder()
 *     .child(ParagraphBlock.of("Inserted after"))
 *     .position(Position.afterBlock("some-block-id"))
 *     .build()
 * }</pre>
 */
@Getter
@Setter
public class AppendBlockChildrenParams {

  private List<Block> children;
  private Position position;

  public AppendBlockChildrenParams() {}

  /**
   * Creates params with multiple child blocks and no position.
   *
   * @param children the blocks to append
   * @return params ready for use
   */
  public static AppendBlockChildrenParams of(List<Block> children) {
    AppendBlockChildrenParams params = new AppendBlockChildrenParams();
    params.setChildren(new ArrayList<>(children));
    return params;
  }

  /**
   * Returns a new builder for constructing params with optional position or incremental child
   * accumulation.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

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
    public Builder children(List<Block> children) {
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
