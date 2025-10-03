package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.RichText;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Represents a heading 2 block in Notion.
 *
 * <p>A heading 2 block displays content as a second-level heading with larger text and formatting.
 * It can optionally be toggleable to show/hide child content and supports rich text formatting.
 *
 * @author KristaxLab
 * @see Block
 * @see RichText
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class HeadingTwoBlock extends Block {
  /**
   * The heading 2 configuration
   */
  @JsonProperty("heading_2")
  private Heading heading2;

  /**
   * Represents the configuration for a heading 2.
   *
   * <p>Contains the text content, color styling, toggleable state, and any nested child blocks that
   * belong to this heading.
   */
  @Data
  public static class Heading {
    /**
     * The rich text content of the heading
     */
    @JsonProperty("rich_text")
    private List<RichText> richText;

    /**
     * The color of the heading text
     */
    @JsonProperty("color")
    private String color;

    /**
     * Whether the heading can be toggled to show/hide children
     */
    @Accessors(fluent = true)
    @JsonProperty("is_toggleable")
    private Boolean isToggleable;

    /**
     * Child blocks nested under this heading
     */
    @JsonProperty("children")
    private List<Block> children;
  }
}
