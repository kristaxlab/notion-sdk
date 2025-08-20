package io.kristixlab.notion.api.model.databases.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for URL columns.
 * Stores web URLs with validation.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UrlDatabaseProperty extends DatabaseProperty {
    // URL properties have no additional configuration
}
