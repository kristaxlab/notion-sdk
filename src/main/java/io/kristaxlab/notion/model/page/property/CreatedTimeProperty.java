package io.kristaxlab.notion.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatedTimeProperty extends PageProperty {
  private final String type = PagePropertyType.CREATED_TIME.type();

  private String createdTime;
}
