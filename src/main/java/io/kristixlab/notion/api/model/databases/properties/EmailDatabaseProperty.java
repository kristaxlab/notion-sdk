package io.kristixlab.notion.api.model.databases.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for email columns.
 * Stores email addresses with validation.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EmailDatabaseProperty extends DatabaseProperty {
    // Email properties have no additional configuration
}
