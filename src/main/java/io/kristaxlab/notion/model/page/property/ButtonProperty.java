package io.kristaxlab.notion.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ButtonProperty extends PageProperty {

  private final String type = PagePropertyType.BUTTON.type();

  private Object button = new Object();
}
