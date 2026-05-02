package io.kristaxlab.notion.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailProperty extends PageProperty {
  private final String type = PagePropertyType.EMAIL.type();

  private String email;
}
