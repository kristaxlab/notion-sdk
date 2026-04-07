package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion to-do block.
 *
 * <p>Simple construction via {@link #of(String)}. For rich text formatting, checked state, nested
 * children, or block color use {@link #builder()}.
 */
@Getter
@Setter
public class ToDoBlock extends Block {

  private ToDo toDo;

  public ToDoBlock() {
    setType("to_do");
    toDo = new ToDo();
  }

  public static ToDoBlock of(String text) {
    ToDoBlock block = new ToDoBlock();
    block.getToDo().setRichText(RichText.of(text));
    return block;
  }

  /**
   * Returns a new builder for constructing a {@link ToDoBlock} with rich text formatting, checked
   * state, block-level color, and/or nested children.
   */
  public static Builder builder() {
    return new Builder();
  }

  public static class Builder extends BlockWithChildren.Builder<Builder, ToDoBlock> {

    private Boolean checked;

    private Builder() {}

    /** Sets the checked state of the to-do item. */
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

  @Getter
  @Setter
  public static class ToDo extends BlockWithChildren {

    private Boolean checked;
  }
}
