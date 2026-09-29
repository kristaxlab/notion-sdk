package io.kristaxlab.notion.model.database;

import io.kristaxlab.notion.model.datasource.properties.DataSourcePropertySchema;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitialDatasource {

  private Map<String, DataSourcePropertySchema> properties = new HashMap<>();

  public static InitialDatasource of(Map<String, DataSourcePropertySchema> properties) {
    InitialDatasource ids = new InitialDatasource();
    ids.setProperties(properties);
    return ids;
  }
}
