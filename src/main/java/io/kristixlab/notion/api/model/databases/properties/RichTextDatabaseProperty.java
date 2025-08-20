package io.kristixlab.notion.api.model.databases.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Database property for rich text columns. Allows formatted text content. */
@Data
@EqualsAndHashCode(callSuper = true)
public class RichTextDatabaseProperty extends DatabaseProperty {

  @JsonProperty("rich_text")
  private RichTextConfig richText = new RichTextConfig();

  @Data
  public static class RichTextConfig {
    // Rich text properties have no additional configuration, but the object must be present
  }
}
