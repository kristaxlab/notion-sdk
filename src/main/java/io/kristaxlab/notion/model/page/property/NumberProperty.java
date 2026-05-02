package io.kristaxlab.notion.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NumberProperty extends PageProperty {
  private final String type = PagePropertyType.NUMBER.type();

  private Number number;
}
