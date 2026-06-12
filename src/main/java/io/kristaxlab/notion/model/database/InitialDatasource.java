package io.kristaxlab.notion.model.database;

import io.kristixlab.notion.api.model.datasources.properties.DataSourcePropertySchemaParams;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitialDatasource {

  private Map<String, DataSourcePropertySchemaParams> properties = new HashMap<>();

  public static InitialDatasource of(Map<String, DataSourcePropertySchemaParams> properties) {
    InitialDatasource ids = new InitialDatasource();
    ids.setProperties(properties);
    return ids;
  }
}
