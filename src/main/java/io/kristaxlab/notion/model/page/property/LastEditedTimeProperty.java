package io.kristaxlab.notion.model.page.property;

import lombok.Getter;
import lombok.Setter;

/** Page property payload for Notion's {@code last_edited_time} system property. */
@Getter
@Setter
public class LastEditedTimeProperty extends PageProperty {
  private final String type = PagePropertyType.LAST_EDITED_TIME.type();

  private String lastEditedTime;
}
