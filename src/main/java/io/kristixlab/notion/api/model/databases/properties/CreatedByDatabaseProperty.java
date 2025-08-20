package io.kristixlab.notion.api.model.databases.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for created_by columns. Automatically populated with the user who created the
 * page.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CreatedByDatabaseProperty extends DatabaseProperty {
  // Created by properties have no additional configuration - auto-generated
}
