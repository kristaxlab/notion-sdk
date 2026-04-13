package io.kristaxlab.notion.model.page.property;

import io.kristaxlab.notion.model.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatedByProperty extends PageProperty {
  private final String type = PagePropertyType.CREATED_BY.type();

  private User createdBy;
}
