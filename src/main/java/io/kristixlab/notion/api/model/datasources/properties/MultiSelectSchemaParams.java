package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for multi-select columns. Allows users to select multiple options from a
 * predefined list.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MultiSelectSchemaParams extends DataSourcePropertySchemaParams {

  @JsonProperty("multi_select")
  private MultiSelectConfig multiSelect = new MultiSelectConfig();

  public static MultiSelectSchemaParams of(SelectOption... options) {
    MultiSelectSchemaParams prop = new MultiSelectSchemaParams();
    MultiSelectConfig config = new MultiSelectConfig();
    config.setOptions(java.util.Arrays.asList(options));
    prop.setMultiSelect(config);
    return prop;
  }

  public static MultiSelectSchemaParams of(String... optionNames) {
    MultiSelectSchemaParams prop = new MultiSelectSchemaParams();
    MultiSelectConfig config = new MultiSelectConfig();
    config.setOptions(new ArrayList<>());
    prop.setMultiSelect(config);
    for (String name : optionNames) {
      SelectOption opt = new SelectOption();
      opt.setName(name);
      config.getOptions().add(opt);
    }
    return prop;
  }

  public static MultiSelectSchemaParams of(List<SelectOption> options) {
    MultiSelectSchemaParams prop = new MultiSelectSchemaParams();
    MultiSelectConfig config = new MultiSelectConfig();
    config.setOptions(new ArrayList<>(options));
    prop.setMultiSelect(config);
    return prop;
  }

  @Data
  public static class MultiSelectConfig {
    @JsonProperty("options")
    private List<SelectOption> options;
  }
}
