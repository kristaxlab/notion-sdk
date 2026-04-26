package io.kristaxlab.notion.model.page.property;

import io.kristaxlab.notion.model.user.User;
import lombok.Getter;
import lombok.Setter;

/** Page property payload for Notion's {@code last_edited_by} system property. */
@Getter
@Setter
public class LastEditedByProperty extends PageProperty {
  private final String type = PagePropertyType.LAST_EDITED_BY.type();

  private User lastEditedBy;
}
