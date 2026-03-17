package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.RichText;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a to-do block in Notion.
 *
 * <p>A to-do block is a checkable item that can be marked as completed or uncompleted. It displays
 * content with a checkbox and supports rich text formatting and nested child blocks for creating
 * task hierarchies.
 *
 * @author KristaxLab
 * @see Block
 * @see RichText
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class ToDoBlock extends Block {
  /** The to-do configuration */
  @JsonProperty("to_do")
  private ToDo toDo;

  public ToDoBlock() {
    setType("to_do");
    toDo = new ToDo();
  }

  public static ToDoBlock of(String text) {
    ToDoBlock block = new ToDoBlock();
    List<RichText> richTexts = RichText.builder().fromText(text).buildAsList();
    block.getToDo().setRichText(richTexts);
    return block;
  }

  /**
   * Represents the configuration for a to-do item.
   *
   * <p>Contains the text content, checked state, color styling, and any nested child blocks that
   * belong to this to-do item.
   */
  @Data
  public static class ToDo {
    /** The rich text content of the to-do item */
    @JsonProperty("rich_text")
    private List<RichText> richText;

    /** Whether the to-do item is checked/completed */
    @JsonProperty("checked")
    private Boolean checked;

    /** The color of the to-do text */
    @JsonProperty("color")
    private String color;

    /** Child blocks nested under this to-do item */
    @JsonProperty("children")
    private List<Block> children;
  }
}
