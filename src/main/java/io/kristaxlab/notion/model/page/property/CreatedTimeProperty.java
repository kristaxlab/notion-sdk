package io.kristaxlab.notion.model.page.property;

import lombok.Getter;
import lombok.Setter;

/** Page property payload for Notion's {@code created_time} system property. */
@Getter
@Setter
public class CreatedTimeProperty extends PagePropertyValue {
  private final String type = PropertyType.CREATED_TIME.type();

  private String createdTime;
}
