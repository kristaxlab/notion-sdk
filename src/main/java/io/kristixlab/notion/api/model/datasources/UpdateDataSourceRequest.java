package io.kristixlab.notion.api.model.datasources;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.FileData;
import io.kristixlab.notion.api.model.common.Icon;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.common.RichText;
import io.kristixlab.notion.api.model.datasources.properties.DatasourceProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Represents a Notion data source object. Contains the data source schema, properties, and
 * metadata.
 */
@Data
public class UpdateDataSourceRequest {

  @JsonProperty("title")
  private List<RichText> title;

  @JsonProperty("icon")
  private Icon icon;

  @JsonProperty("cover")
  private FileData cover;

  @JsonProperty("properties")
  private Map<String, DatasourceProperty> properties;

  @JsonProperty("parent")
  private Parent parent;

  @JsonProperty("database_parent")
  private Parent databaseParent;

  @JsonProperty("is_inline")
  private Boolean isInline;
}
