package io.kristixlab.notion.api.model.page.property;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultiSelectProperty extends PageProperty {
  private final String type = PagePropertyType.MULTI_SELECT.type();

  private List<SelectValue> multiSelect;

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

  public MultiSelectProperty add(SelectValue option) {
    if (this.multiSelect == null) {
      this.multiSelect = new ArrayList<>();
    }
    this.multiSelect.add(option);
    return this;
  }

  // TODO id
  public MultiSelectProperty add(String name) {
    return add(SelectValue.ofName(name));
  }

  public MultiSelectProperty add(String id, String name) {
    return add(SelectValue.ofName(name));
  }
}
