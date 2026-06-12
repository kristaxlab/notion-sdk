package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import lombok.Getter;
import lombok.Setter;

/** Database property for relation columns. Links to pages in another database/data source. */
@Getter
@Setter
public class RelationSchema extends DataSourcePropertySchema {

  private final String type = PropertyType.RELATION.type();

  private RelationConfig relation = new RelationConfig();

  @Getter
  @Setter
  public static class RelationConfig {

    // TODO only in reponse
    private String databaseId;

    private String dataSourceId;

    private String type; // "single_property" or "dual_property"

    private SinglePropertyConfig singleProperty;

    private DualPropertyConfig dualProperty;
  }

  @Getter
  @Setter
  public static class SinglePropertyConfig {
    // Empty object for single property relations
  }

  @Getter
  @Setter
  public static class DualPropertyConfig {

    private String syncedPropertyName;

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
