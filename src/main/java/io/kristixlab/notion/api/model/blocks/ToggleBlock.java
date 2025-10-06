package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.RichText;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a toggle block in Notion.
 *
 * <p>A toggle block displays content with a collapsible/expandable section. Users can click on the
 * toggle to show or hide the child content, making it useful for organizing information and
 * creating expandable sections.
 *
 * @author KristaxLab
 * @see Block
 * @see RichText
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class ToggleBlock extends Block {
  /** The toggle configuration */
  @JsonProperty("toggle")
  private Toggle toggle;

  /**
   * Represents the configuration for a toggle block.
   *
   * <p>Contains the text content, color styling, and any nested child blocks that can be toggled to
   * show/hide.
   */
  @Data
  public static class Toggle {
    /** The rich text content of the toggle header */
    @JsonProperty("rich_text")
    private List<RichText> richText;

    /** The color of the toggle text */
    @JsonProperty("color")
    private String color;

    /** Child blocks that can be toggled to show/hide */
    @JsonProperty("children")
    private List<Block> children;
  }
}
