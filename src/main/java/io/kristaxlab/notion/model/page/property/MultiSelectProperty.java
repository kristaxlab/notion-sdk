package io.kristaxlab.notion.model.page.property;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultiSelectProperty extends PageProperty {
  private final String type = PagePropertyType.MULTI_SELECT.type();

  private List<SelectValue> multiSelect;
}
