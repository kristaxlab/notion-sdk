package io.kristixlab.notion.api.model.databases.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for created_time columns. Automatically populated with the timestamp when the
 * page was created.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CreatedTimeDatabaseProperty extends DatabaseProperty {
  // Created time properties have no additional configuration - auto-generated
}
