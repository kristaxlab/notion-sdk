package io.kristixlab.notion.api.model.databases.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Database property for multi-select columns.
 * Allows users to select multiple options from a predefined list.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MultiSelectDatabaseProperty extends DatabaseProperty {

    @JsonProperty("multi_select")
    private MultiSelectConfig multiSelect;

    @Data
    public static class MultiSelectConfig {
        @JsonProperty("options")
        private List<SelectOption> options;
    }

    @Data
    public static class SelectOption {
        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("color")
        private String color; // "default", "gray", "brown", "orange", "yellow", "green", "blue", "purple", "pink", "red"

        @JsonProperty("description")
        private String description;
    }
}
