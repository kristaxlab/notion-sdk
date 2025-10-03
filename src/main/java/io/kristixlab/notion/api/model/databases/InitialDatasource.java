package io.kristixlab.notion.api.model.databases;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.datasources.properties.DatasourceProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class InitialDatasource {
  @JsonProperty("properties")
  private Map<String, DatasourceProperty> properties = new HashMap<>();

  public static InitialDatasource of(Map<String, DatasourceProperty> properties) {
    InitialDatasource ids = new InitialDatasource();
    ids.setProperties(properties);
    return ids;
  }
}
