package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Database property for select columns. Allows users to select one option from a predefined list.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SelectDataSourcePropertySchema extends DataSourcePropertySchema {

  @JsonProperty("select")
  private SelectConfig select = new SelectConfig();

  @Data
  public static class SelectConfig {
    @JsonProperty("options")
    private List<SelectOption> options;
  }
}
