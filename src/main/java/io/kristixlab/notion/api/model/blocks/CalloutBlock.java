package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.Icon;
import io.kristixlab.notion.api.model.common.RichText;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Represents a callout block in Notion.
 *
 * <p>A callout block displays content in a highlighted box with an optional icon. It's used to draw
 * attention to important information, notes, warnings, or tips. Supports rich text formatting,
 * custom icons, and nested child blocks.
 *
 * @author KristaxLab
 * @see Block
 * @see RichText
 * @see Icon
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class CalloutBlock extends Block {

  /**
   * The callout configuration
   */
  @JsonProperty("callout")
  private Callout callout;

  /**
   * Represents the configuration for a callout block.
   *
   * <p>Contains the text content, optional icon, color styling, and any nested child blocks that
   * belong to this callout.
   */
  @Data
  public static class Callout {
    /**
     * The rich text content of the callout
     */
    @JsonProperty("rich_text")
    private List<RichText> richText;

    /**
     * The icon displayed with the callout
     */
    @JsonProperty("icon")
    private Icon icon;

    /**
     * The color of the callout background and text
     */
    @JsonProperty("color")
    private String color;

    /**
     * Child blocks nested under this callout
     */
    @JsonProperty("children")
    private List<Block> children;
  }
}
