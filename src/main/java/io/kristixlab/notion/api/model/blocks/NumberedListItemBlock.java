package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.RichText;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a numbered list item block in Notion.
 *
 * <p>A numbered list item is a block that displays content with an automatic numbering. It can
 * contain rich text content and supports nested child blocks to create hierarchical numbered list
 * structures.
 *
 * @author KristaxLab
 * @see Block
 * @see RichText
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class NumberedListItemBlock extends Block {
  /** The numbered list item configuration */
  @JsonProperty("numbered_list_item")
  private NumberedListItem numberedListItem;

  public NumberedListItemBlock() {
    setType("numbered_list_item");
    numberedListItem = new NumberedListItem();
  }

  public static NumberedListItemBlock of(String text) {
    NumberedListItemBlock block = new NumberedListItemBlock();
    block.getNumberedListItem().setRichText(RichText.builder().fromText(text).buildAsList());
    return block;
  }

  /**
   * Represents the configuration for a numbered list item.
   *
   * <p>Contains the text content, color styling, and any nested child blocks that belong to this
   * numbered list item.
   */
  @Data
  public static class NumberedListItem {
    /** The rich text content of the list item */
    @JsonProperty("rich_text")
    private List<RichText> richText;

    /** The color of the list item text */
    @JsonProperty("color")
    private String color;

    /** Child blocks nested under this list item */
    @JsonProperty("children")
    private List<Block> children;
  }
}
