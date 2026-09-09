package io.kristaxlab.notion.model.datasource.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * Fallback database property for unknown or unsupported property types. Used when the API returns a
 * property type that is not yet supported by this SDK.
 */
@Getter
@Setter
public class UnknownDataSourcePropertySchema extends DataSourcePropertySchema {

  private String type;
  // Unknown properties store only the basic fields from the base class
}
