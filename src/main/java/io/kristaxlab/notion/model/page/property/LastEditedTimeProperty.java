package io.kristaxlab.notion.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LastEditedTimeProperty extends PageProperty {
  private final String type = PagePropertyType.LAST_EDITED_TIME.type();

  private String lastEditedTime;
}
