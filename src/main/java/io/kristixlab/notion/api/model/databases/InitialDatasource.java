package io.kristixlab.notion.api.model.databases;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.datasources.properties.DataSourcePropertySchema;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class InitialDatasource {
  @JsonProperty("properties")
  private Map<String, DataSourcePropertySchema> properties = new HashMap<>();

  public static InitialDatasource of(Map<String, DataSourcePropertySchema> properties) {
    InitialDatasource ids = new InitialDatasource();
    ids.setProperties(properties);
    return ids;
  }
}
