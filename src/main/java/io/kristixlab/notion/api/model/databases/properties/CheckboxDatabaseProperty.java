package io.kristixlab.notion.api.model.databases.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for checkbox columns.
 * Simple boolean property with no additional configuration.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CheckboxDatabaseProperty extends DatabaseProperty {

    @JsonProperty("checkbox")
    private CheckboxConfig checkbox = new CheckboxConfig();

    @Data
    public static class CheckboxConfig {

    }

}

