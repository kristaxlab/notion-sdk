package io.kristixlab.notion.api.model.databases.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for last_edited_time columns.
 * Automatically populated with the timestamp when the page was last edited.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LastEditedTimeDatabaseProperty extends DatabaseProperty {
    // Last edited time properties have no additional configuration - auto-generated
}
