package io.kristixlab.notion.api.model.datasources;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.*;
import io.kristixlab.notion.api.model.datasources.properties.DataSourcePropertySchema;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a Notion data source object. Contains the data source schema, properties, and
 * metadata.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DataSource extends NotionObjectType {

  @JsonProperty("title")
  private List<RichText> title;

  @JsonProperty("description")
  private List<RichText> description;

  @JsonProperty("database_parent")
  private Parent databaseParent;

  @JsonProperty("icon")
  private Icon icon;

  @JsonProperty("cover")
  private Cover cover;

  @JsonProperty("properties")
  private Map<String, DataSourcePropertySchema> properties;

  @JsonProperty("is_inline")
  private Boolean isInline;

  @JsonProperty("url")
  private String url;

  @JsonProperty("public_url")
  private String publicUrl;
}
