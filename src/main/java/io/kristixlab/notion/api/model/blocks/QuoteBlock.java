package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.RichText;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Represents a quote block in Notion.
 *
 * <p>A quote block displays content with special formatting to indicate quoted text. It typically
 * appears with indentation and different styling to distinguish it from regular text content.
 * Supports rich text formatting and nested child blocks.
 *
 * @author KristaxLab
 * @see Block
 * @see RichText
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class QuoteBlock extends Block {

  /**
   * The quote configuration
   */
  @JsonProperty("quote")
  private Quote quote;

  /**
   * Represents the configuration for a quote block.
   *
   * <p>Contains the text content, color styling, and any nested child blocks that belong to this
   * quote.
   */
  @Data
  public static class Quote {
    /**
     * The rich text content of the quote
     */
    @JsonProperty("rich_text")
    private List<RichText> richText;

    /**
     * The color of the quote text
     */
    @JsonProperty("color")
    private String color;

    /**
     * Child blocks nested under this quote
     */
    @JsonProperty("children")
    private List<Block> children;
  }
}
