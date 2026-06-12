package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import lombok.Getter;
import lombok.Setter;

/** Database property for number columns. Supports different number formats. */
@Getter
@Setter
public class NumberSchema extends DataSourcePropertySchema {

  private final String type = PropertyType.NUMBER.type();

  private NumberFormat number = new NumberFormat();

  @Getter
  @Setter
  public static class NumberFormat {

    private String format;
  }
}
