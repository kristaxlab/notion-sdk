package io.kristixlab.notion.api.model.databases;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.datasources.properties.DataSourcePropertySchemaParams;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class InitialDatasource {
  @JsonProperty("properties")
  private Map<String, DataSourcePropertySchemaParams> properties = new HashMap<>();

  public static InitialDatasource of(Map<String, DataSourcePropertySchemaParams> properties) {
    InitialDatasource ids = new InitialDatasource();
    ids.setProperties(properties);
    return ids;
  }

}
