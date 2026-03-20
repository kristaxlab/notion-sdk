package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for place (location) columns. Contains geographical information including
 * coordinates, name, address, and location provider IDs.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PlaceSchema extends DataSourcePropertySchema {

  @JsonProperty("place")
  private Object place = new Object();


}
