package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import lombok.Getter;
import lombok.Setter;

/**
 * Database property for place (location) columns. Contains geographical information including
 * coordinates, name, address, and location provider IDs.
 */
@Getter
@Setter
public class PlaceSchema extends DataSourcePropertySchema {

  public PlaceSchema() {
    setType(PropertyType.PLACE.type());
    place = new Object();
  }

  private Object place;
}
