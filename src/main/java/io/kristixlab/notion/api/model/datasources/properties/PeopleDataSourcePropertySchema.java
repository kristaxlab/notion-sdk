package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Database property for people columns. Allows selecting users from the workspace. */
@Data
@EqualsAndHashCode(callSuper = true)
public class PeopleDataSourcePropertySchema extends DataSourcePropertySchema {

  @JsonProperty("people")
  private Object people = new Object();
}
