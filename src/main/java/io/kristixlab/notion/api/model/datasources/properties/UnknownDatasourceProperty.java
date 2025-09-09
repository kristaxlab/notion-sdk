package io.kristixlab.notion.api.model.datasources.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Fallback database property for unknown or unsupported property types. Used when the API returns a
 * property type that is not yet supported by this SDK.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UnknownDatasourceProperty extends DatasourceProperty {
  // Unknown properties store only the basic fields from the base class
}
