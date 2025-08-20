package io.kristixlab.notion.api.model.databases.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for files columns.
 * Allows uploading and storing files.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FilesDatabaseProperty extends DatabaseProperty {
    // Files properties have no additional configuration
}
