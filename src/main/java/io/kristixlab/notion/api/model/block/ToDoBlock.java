package io.kristixlab.notion.api.model.block;

import io.kristixlab.notion.api.model.helper.NotionBlocks;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion to-do block.
 *
 * <p>Simple construction via {@link NotionBlocks#todo(String) Blocks.todo(...)}. For rich text
 * formatting, checked state, nested children, or block color use {@link #builder()}.
 */
@Getter
@Setter
public class ToDoBlock extends Block {

  private ToDo toDo;

  public ToDoBlock() {
    setType(BlockType.TO_DO.getValue());
    toDo = new ToDo();
  }

  /**
   * Returns a new builder for constructing a {@link ToDoBlock} with rich text formatting, checked
   * state, block-level color, and/or nested children.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link ToDoBlock}. */
  public static class Builder extends BlockWithChildren.Builder<Builder, ToDoBlock> {

    private Boolean checked;

    private Builder() {}

    /**
     * Marks the to-do item as checked. Equivalent to {@code checked(true)}.
     *
     * @return this builder
     */
    public Builder checked() {
      return checked(true);
    }

    /**
     * Sets the checked state of the to-do item.
     *
     * @param checked {@code true} for checked, {@code false} for unchecked
     * @return this builder
     */
    public Builder checked(boolean checked) {
      this.checked = checked;
      return self();
    }

    @Override
    public ToDoBlock build() {
      ToDoBlock block = new ToDoBlock();
      buildContent(block.getToDo());
      if (checked != null) {
        block.getToDo().setChecked(checked);
      }
      return block;
    }
  }

  /** The inner content object of a to-do block. */
  @Getter
  @Setter
  public static final class ToDo extends BlockWithChildren {

    private Boolean checked;
  }
}
