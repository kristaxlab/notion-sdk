package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import lombok.Getter;
import lombok.Setter;

/** Database property for people columns. Allows selecting users from the workspace. */
@Getter
@Setter
public class PeopleSchema extends DataSourcePropertySchema {

  public PeopleSchema() {
    setType(PropertyType.PEOPLE.type());
    people = new Object();
  }

  private Object people;
}
