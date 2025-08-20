package io.kristixlab.notion.api.model.databases.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for people columns.
 * Allows selecting users from the workspace.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PeopleDatabaseProperty extends DatabaseProperty {
    // People properties have no additional configuration
}
