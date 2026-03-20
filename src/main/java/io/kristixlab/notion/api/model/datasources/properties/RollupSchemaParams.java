package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for rollup columns. Aggregates values from a property in a related database.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RollupSchemaParams extends DataSourcePropertySchemaParams {

  @JsonProperty("rollup")
  private RollupConfig rollup = new RollupConfig();

  public static RollupSchemaParams of(
      String relationName, String rollupName, String rollupFunction) {
    RollupSchemaParams prop = new RollupSchemaParams();
    RollupConfig config = new RollupConfig();
    config.setRelationPropertyName(relationName);
    config.setRollupPropertyName(rollupName);
    config.setFunction(rollupFunction);
    prop.setRollup(config);
    return prop;
  }

  public static RollupSchemaParams.Builder builder() {
    return new Builder();
  }

  @Data
  public static class RollupConfig {
    @JsonProperty("relation_property_name")
    private String relationPropertyName;

    @JsonProperty("relation_property_id")
    private String relationPropertyId;

    @JsonProperty("rollup_property_name")
    private String rollupPropertyName;

    @JsonProperty("rollup_property_id")
    private String rollupPropertyId;

    @JsonProperty("function")
    private String function;
  }

  public static class Builder {
    private String relationNameOrId;
    private String propertyNameOrId;
    private String function;

    public Builder relation(String nameOrId) {
      this.relationNameOrId = nameOrId;
      return this;
    }

    public Builder property(String nameOrId) {
      this.propertyNameOrId = nameOrId;
      return this;
    }

    public Builder calculate(String function) {
      this.function = function;
      return this;
    }

    public Builder calculate(RollupFunctionType function) {
      this.function = function.getValue();
      return this;
    }

    public RollupSchemaParams build() {
      if (this.relationNameOrId == null || this.propertyNameOrId == null || this.function == null) {
        throw new IllegalStateException(
            "Relation name/id, property name/id and function must be provided");
      }
      RollupSchemaParams prop = new RollupSchemaParams();
      RollupConfig config = new RollupConfig();
      // Notion API accepts either name or id for relation and rollup properties ids
      config.setRelationPropertyId(this.relationNameOrId);
      config.setRollupPropertyId(this.propertyNameOrId);
      config.setFunction(this.function);
      prop.setRollup(config);
      return prop;
    }
  }
}
