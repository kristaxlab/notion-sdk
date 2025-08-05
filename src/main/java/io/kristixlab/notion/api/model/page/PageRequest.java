package io.kristixlab.notion.api.model.page;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageRequest {
    @JsonProperty("parent")
    private Parent parent;

    @JsonProperty("properties")
    private Map<String, Object> properties;

    @JsonProperty("children")
    private Object[] children;

    // Getters and setters

    public static class Parent {
        @JsonProperty("database_id")
        private String databaseId;

        @JsonProperty("type")
        private String type = "database_id";

        // Getters and setters
    }
}