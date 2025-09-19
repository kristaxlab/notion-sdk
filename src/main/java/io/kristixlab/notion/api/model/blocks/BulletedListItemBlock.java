package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.RichText;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a bulleted list item block in Notion.
 * <p>
 * A bulleted list item is a block that displays content with a bullet point.
 * It can contain rich text content and supports nested child blocks to create
 * hierarchical list structures.
 * </p>
 *
 * @author KristaxLab
 * @since 1.0
 * @see Block
 * @see RichText
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class BulletedListItemBlock extends Block {
  /** The bulleted list item configuration */
  @JsonProperty("bulleted_list_item")
  private BulletedListItem bulletedListItem;

  /**
   * Represents the configuration for a bulleted list item.
   * <p>
   * Contains the text content, color styling, and any nested child blocks
   * that belong to this list item.
   * </p>
   */
  @Data
  public static class BulletedListItem {
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
