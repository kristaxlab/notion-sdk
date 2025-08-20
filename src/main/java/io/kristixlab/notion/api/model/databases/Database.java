package io.kristixlab.notion.api.model.databases;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.BaseNotionResponse;
import io.kristixlab.notion.api.model.common.Icon;
import io.kristixlab.notion.api.model.common.NotionObject;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.common.RichText;
import io.kristixlab.notion.api.model.databases.properties.DatabaseProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * Represents a Notion database object.
 * Contains the database schema, properties, and metadata.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Database extends NotionObject {

    @JsonProperty("title")
    private List<RichText> title;

    @JsonProperty("description")
    private List<RichText> description;

    @JsonProperty("icon")
    private Icon icon;

    @JsonProperty("cover")
    private Object cover; // Cover object

    @JsonProperty("properties")
    private Map<String, DatabaseProperty> properties;

    @JsonProperty("url")
    private String url;

    @JsonProperty("public_url")
    private String publicUrl;

    @JsonProperty("is_inline")
    private Boolean isInline;
}
