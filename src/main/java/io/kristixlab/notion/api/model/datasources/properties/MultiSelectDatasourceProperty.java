package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for multi-select columns. Allows users to select multiple options from a
 * predefined list.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MultiSelectDatasourceProperty extends DatasourceProperty {

  @JsonProperty("multi_select")
  private MultiSelectConfig multiSelect = new MultiSelectConfig();

  @Data
  public static class MultiSelectConfig {
    @JsonProperty("options")
    private List<SelectOption> selectOptions;
  }
}
