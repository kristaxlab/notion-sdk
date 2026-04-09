package io.kristixlab.notion.api.model.page.property.list;

import io.kristixlab.notion.api.model.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListedPeopleProperty extends ListedPageProperty {

  private User people;
}
