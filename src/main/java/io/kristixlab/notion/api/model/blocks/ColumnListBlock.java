package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a column list block in Notion.
 *
 * <p>A column list block is a container that holds multiple columns side by side. It's used to
 * create multi-column layouts in Notion pages. The column list itself doesn't contain content
 * directly, but serves as a parent for individual column blocks.
 *
 * @author KristaxLab
 * @see Block
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class ColumnListBlock extends Block {
  /**
   * The column list configuration
   */
  @JsonProperty("column_list")
  private ColumnList columnList;

  /**
   * Represents the configuration for a column list.
   *
   * <p>Currently, this is an empty object as column lists don't have additional properties beyond
   * the standard block properties.
   */
  @Data
  public static class ColumnList {
    // No properties, just an empty object
  }
}
