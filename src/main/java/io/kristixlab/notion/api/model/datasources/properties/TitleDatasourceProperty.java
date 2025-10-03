package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for title columns. Every database must have exactly one title property.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TitleDatasourceProperty extends DatasourceProperty {

  @JsonProperty("title")
  private Object title = new Object();
}
