package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.users.User;
import io.kristixlab.notion.api.util.PagePropertyType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LastEditedByProperty extends PageProperty {
  private final String type = PagePropertyType.LAST_EDITED_BY.type();

  @JsonProperty("last_edited_by")
  private User lastEditedBy;

  public static LastEditedByProperty of(User lastEditedBy) {
    LastEditedByProperty property = new LastEditedByProperty();
    property.setLastEditedBy(lastEditedBy);
    return property;
  }
}
