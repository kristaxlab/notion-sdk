package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Database property for relation columns. Links to pages in another database/data source. */
@Data
@EqualsAndHashCode(callSuper = true)
public class RelationSchema extends DataSourcePropertySchema {

  @JsonProperty("relation")
  private RelationConfig relation = new RelationConfig();

  @Data
  public static class RelationConfig {

    // TODO only in reponse
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

  public static class Builder {
    private String name;
    private String dataSourceId;
    private String type; // "single_property" or "dual_property"
    private String syncedPropertyName;
    private String syncedPropertyId;

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder dataSourceId(String dataSourceId) {
      this.dataSourceId = dataSourceId;
      return this;
    }

    public Builder type(String type) {
      this.type = type;
      return this;
    }

    public Builder syncedPropertyName(String syncedPropertyName) {
      this.syncedPropertyName = syncedPropertyName;
      return this;
    }

    public Builder syncedPropertyId(String syncedPropertyId) {
      this.syncedPropertyId = syncedPropertyId;
      return this;
    }

    public RelationSchema build() {
      RelationSchema prop = new RelationSchema();
      prop.setName(name);
      RelationConfig config = new RelationConfig();
      config.setDataSourceId(dataSourceId);
      config.setType(type);
      if ("dual_property".equals(type)) {
        DualPropertyConfig dualConfig = new DualPropertyConfig();
        dualConfig.setSyncedPropertyName(syncedPropertyName);
        dualConfig.setSyncedPropertyId(syncedPropertyId);
        config.setDualProperty(dualConfig);
      } else {
        config.setSingleProperty(new SinglePropertyConfig());
      }
      prop.setRelation(config);
      return prop;
    }
  }
}
