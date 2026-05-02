package io.kristaxlab.notion.model.page.property;

import io.kristaxlab.notion.model.user.User;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeopleProperty extends PageProperty {
  private final String type = PagePropertyType.PEOPLE.type();

  private List<User> people;
}
