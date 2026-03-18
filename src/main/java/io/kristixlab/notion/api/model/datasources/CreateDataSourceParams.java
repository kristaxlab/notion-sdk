package io.kristixlab.notion.api.model.datasources;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.*;
import io.kristixlab.notion.api.model.datasources.properties.DataSourcePropertySchema;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * Represents a Notion data source object. Contains the data source schema, properties, and
 * metadata.
 */
@Data
public class CreateDataSourceParams {

  @JsonProperty("parent")
  private Parent parent;

  @JsonProperty("title")
  private List<RichText> title;

  @JsonProperty("properties")
  private Map<String, DataSourcePropertySchema> properties;

  @JsonProperty("icon")
  private IconParams icon;
}
