package io.kristixlab.notion.api.model.page.property;

import io.kristixlab.notion.api.model.users.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatedByProperty extends PageProperty {
  private final String type = PagePropertyType.CREATED_BY.type();

  private User createdBy;
}
