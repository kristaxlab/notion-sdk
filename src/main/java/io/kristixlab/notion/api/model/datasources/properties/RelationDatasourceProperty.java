package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for relation columns. Links to pages in another database/data source.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RelationDatasourceProperty extends DatasourceProperty {

  @JsonProperty("relation")
  private RelationConfig relation = new RelationConfig();

  @Data
  public static class RelationConfig {
    @JsonProperty("database_id")
    private String databaseId;

    @JsonProperty("data_source_id")
    private String dataSourceId;

    @JsonProperty("type")
    private String type; // "single_property" or "dual_property"

    @JsonProperty("single_property")
    private SinglePropertyConfig singleProperty;

    @JsonProperty("dual_property")
    private DualPropertyConfig dualProperty;
  }

  @Data
  public static class SinglePropertyConfig {
    // Empty object for single property relations
  }

  @Data
  public static class DualPropertyConfig {
    @JsonProperty("synced_property_name")
    private String syncedPropertyName;

    @JsonProperty("synced_property_id")
    private String syncedPropertyId;
  }
}
