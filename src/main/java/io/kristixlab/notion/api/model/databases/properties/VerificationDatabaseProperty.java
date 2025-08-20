package io.kristixlab.notion.api.model.databases.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for verification columns.
 * Allows verification of information with approval workflow.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VerificationDatabaseProperty extends DatabaseProperty {
    // Verification properties have no additional configuration
}
