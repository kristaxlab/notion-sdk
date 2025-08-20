package io.kristixlab.notion.api.model.databases.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for title columns.
 * Every database must have exactly one title property.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TitleDatabaseProperty extends DatabaseProperty {

    @JsonProperty("title")
    private TitleConfig title = new TitleConfig();

    @Data
    public static class TitleConfig {
        // Title properties have no additional configuration, but the object must be present
    }
}
