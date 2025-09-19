package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.Color;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MultiSelectProperty extends PageProperty {
  private final String type = "multi_select";

  @JsonProperty("multi_select")
  private List<SelectValue> multiSelect;

  public MultiSelectProperty add(SelectValue option) {
    if (this.multiSelect == null) {
      this.multiSelect = new ArrayList<>();
    }
    this.multiSelect.add(option);
    return this;
  }

  public MultiSelectProperty add(String name, Color color) {
    return add(SelectValue.of(null, name, color));
  }

  public MultiSelectProperty add(String id, String name, Color color) {
    return add(SelectValue.of(id, name, color));
  }

  public static MultiSelectProperty of(List<SelectValue> options) {
    MultiSelectProperty property = new MultiSelectProperty();
    property.setMultiSelect(options);
    return property;
  }

  public static MultiSelectProperty of(SelectValue... options) {
    MultiSelectProperty property = new MultiSelectProperty();
    for (SelectValue option : options) {
      property.add(option);
    }
    return property;
  }
}
