package io.kristaxlab.notion.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectProperty extends PageProperty {
  private final String type = PagePropertyType.SELECT.type();

  private SelectValue select;
}
