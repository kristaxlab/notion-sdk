package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for select columns. Allows users to select one option from a predefined list.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SelectSchemaParams extends DataSourcePropertySchemaParams {

  @JsonProperty("select")
  private SelectConfig select = new SelectConfig();

  public static SelectSchemaParams of(SelectOption... options) {
    SelectSchemaParams prop = new SelectSchemaParams();
    SelectConfig config = new SelectConfig();
    config.setOptions(Arrays.asList(options));
    prop.setSelect(config);
    return prop;
  }

  public static SelectSchemaParams of(String... optionNames) {
    SelectSchemaParams prop = new SelectSchemaParams();
    SelectSchemaParams.SelectConfig config = new SelectSchemaParams.SelectConfig();
    config.setOptions(new ArrayList<>());
    prop.setSelect(config);
    for (String name : optionNames) {
      SelectOption opt = new SelectOption();
      opt.setName(name);
      config.getOptions().add(opt);
    }
    return prop;
  }

  public static SelectSchemaParams of(String optionName, Color color) {
    SelectSchemaParams prop = new SelectSchemaParams();
    SelectConfig config = new SelectConfig();
    SelectOption opt = new SelectOption();
    opt.setName(optionName);
    opt.setColor(color.getValue());
    config.getOptions().add(opt);
    prop.setSelect(config);
    return prop;
  }

  @Data
  public static class SelectConfig {
    @JsonProperty("options")
    private List<SelectOption> options;
  }
}
