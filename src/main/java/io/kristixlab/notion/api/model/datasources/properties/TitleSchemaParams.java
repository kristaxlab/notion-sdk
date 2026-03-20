package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.PropertyType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** Database property for title columns. Every database must have exactly one title property. */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TitleSchemaParams extends DataSourcePropertySchemaParams {

  @JsonProperty("title")
  private Object title = new Object();

  public TitleSchemaParams(String name) {
    super(name);
  }

  @Override
  public String getType() {
    return PropertyType.TITLE.getValue();
  }
}
