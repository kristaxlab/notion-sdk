package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Database property for relation columns. Links to pages in another database/data source. */
@Data
@EqualsAndHashCode(callSuper = true)
public class RelationSchemaParams extends DataSourcePropertySchemaParams {

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

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String name;
    private String dataSourceId;
    private String type; // "single_property" or "dual_property"

    // Notion API supports both id or name set as synced_property_id
    private String syncedPropertyNmaeOrId;

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder dataSourceId(String dataSourceId) {
      this.dataSourceId = dataSourceId;
      return this;
    }

    public Builder typeSingleProperty() {
      return type(RelationType.SINGLE_PROPERTY);
    }

    public Builder typeDualProperty() {
      return type(RelationType.DUAL_PROPERTY);
    }

    public Builder type(RelationType type) {
      return type(type.getValue());
    }

    public Builder type(String type) {
      this.type = type;
      return this;
    }

    public Builder syncedProperty(String syncedPropertyNameOrId) {
      typeDualProperty();
      this.syncedPropertyNmaeOrId = syncedPropertyNameOrId;
      return this;
    }

    public RelationSchemaParams build() {
      if (dataSourceId == null) {
        throw new IllegalStateException("Data source id is required for relation properties");
      }
      if (RelationType.DUAL_PROPERTY.getValue().equals(type)) {
        if (syncedPropertyNmaeOrId == null) {
          throw new IllegalStateException(
              "Dual property relations require synced property name and id");
        }
      }
      RelationSchemaParams prop = new RelationSchemaParams();
      prop.setName(name);
      RelationConfig config = new RelationConfig();
      config.setDataSourceId(dataSourceId);
      config.setType(type);
      if (RelationType.DUAL_PROPERTY.getValue().equals(type)) {
        DualPropertyConfig dualConfig = new DualPropertyConfig();
        dualConfig.setSyncedPropertyId(syncedPropertyNmaeOrId);
        config.setDualProperty(dualConfig);
      } else {
        config.setSingleProperty(new SinglePropertyConfig());
      }
      prop.setRelation(config);
      return prop;
    }
  }
}
