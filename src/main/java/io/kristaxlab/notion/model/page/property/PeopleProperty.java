package io.kristaxlab.notion.model.page.property;

import io.kristaxlab.notion.model.user.User;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeopleProperty extends PagePropertyValue {
  private final String type = PropertyType.PEOPLE.type();

  private List<User> people;
}
