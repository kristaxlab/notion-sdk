package io.kristixlab.notion.api.model.datasources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.*;
import io.kristixlab.notion.api.model.datasources.properties.DataSourcePropertySchema;
import java.util.List;
import java.util.Map;

import io.kristixlab.notion.api.model.datasources.properties.DataSourcePropertySchemaParams;
import lombok.Data;

/**
 * Represents a Notion data source object. Contains the data source schema, properties, and
 * metadata.
 */
@Data
public class UpdateDataSourceParams {

  @JsonProperty("parent")
  private Parent parent;

  @JsonProperty("title")
  private List<RichText> title;

  @JsonProperty("properties")
  @JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.ALWAYS)
  private Map<String, DataSourcePropertySchemaParams> properties;

  @JsonProperty("icon")
  private IconParams icon;

  @JsonProperty("in_trash")
  private Boolean inTrash;
}
