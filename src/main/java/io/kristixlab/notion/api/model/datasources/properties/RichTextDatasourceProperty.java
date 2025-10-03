package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for rich text columns. Allows formatted text content.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RichTextDatasourceProperty extends DatasourceProperty {

  @JsonProperty("rich_text")
  private Object richText = new Object();
}
