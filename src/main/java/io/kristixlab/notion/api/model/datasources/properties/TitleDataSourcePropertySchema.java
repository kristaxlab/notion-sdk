package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** Database property for title columns. Every database must have exactly one title property. */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TitleDataSourcePropertySchema extends DataSourcePropertySchema {

  @JsonProperty("title")
  private Object title = new Object();

  public TitleDataSourcePropertySchema(String name) {
    super(name);
    setType("title");
  }
}
