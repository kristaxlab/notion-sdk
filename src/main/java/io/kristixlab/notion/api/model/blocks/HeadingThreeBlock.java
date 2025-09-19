package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.RichText;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Represents a heading 3 block in Notion.
 *
 * <p>A heading 3 block displays content as a third-level heading with medium text and formatting.
 * It can optionally be toggleable to show/hide child content and supports rich text formatting.
 *
 * @author KristaxLab
 * @since 1.0
 * @see Block
 * @see RichText
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class HeadingThreeBlock extends Block {
  /** The heading 3 configuration */
  @JsonProperty("heading_3")
  private Heading heading3;

  /**
   * Represents the configuration for a heading 3.
   *
   * <p>Contains the text content, color styling, toggleable state, and any nested child blocks that
   * belong to this heading.
   */
  @Data
  public static class Heading {
    /** The rich text content of the heading */
    @JsonProperty("rich_text")
    private List<RichText> richText;

    /** The color of the heading text */
    @JsonProperty("color")
    private String color;

    /** Whether the heading can be toggled to show/hide children */
    @Accessors(fluent = true)
    @JsonProperty("is_toggleable")
    private Boolean isToggleable;

    /** Child blocks nested under this heading */
    @JsonProperty("children")
    private List<Block> children;
  }
}
