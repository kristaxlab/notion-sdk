package io.kristaxlab.notion.model.page.property.list;

import io.kristaxlab.notion.model.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListedPeopleProperty extends ListedPageProperty {

  private User people;
}
