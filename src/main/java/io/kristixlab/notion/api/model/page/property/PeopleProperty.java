package io.kristixlab.notion.api.model.page.property;

import io.kristixlab.notion.api.model.user.User;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeopleProperty extends PageProperty {
  private final String type = PagePropertyType.PEOPLE.type();

  private List<User> people;

  /** Creates a property from a list of Notion user IDs. */
  public static PeopleProperty of(String... userIds) {
    PeopleProperty property = new PeopleProperty();
    List<User> users = new ArrayList<>();
    for (String userId : userIds) {
      User u = new User();
      u.setId(userId);
      users.add(u);
    }
    property.setPeople(users);
    return property;
  }

  /** Creates a property from a list of Notion user IDs. */
  public static PeopleProperty of(List<User> users) {
    PeopleProperty property = new PeopleProperty();
    property.setPeople(new ArrayList<>(users));
    return property;
  }
}
