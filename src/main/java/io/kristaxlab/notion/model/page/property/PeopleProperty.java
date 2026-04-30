package io.kristaxlab.notion.model.page.property;

import io.kristaxlab.notion.model.user.User;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeopleProperty extends PageProperty {
  private final String type = PagePropertyType.PEOPLE.type();

  private List<User> people;

  /** Creates a property from one or more Notion user IDs. */
  public static PeopleProperty of(String... userIds) {
    return of(Arrays.asList(userIds));
  }

  /** Creates a property from a list of Notion user IDs. */
  public static PeopleProperty of(List<String> userIds) {
    PeopleProperty property = new PeopleProperty();
    property.setPeople(
        userIds.stream()
            .map(
                id -> {
                  User u = new User();
                  u.setId(id);
                  return u;
                })
            .collect(Collectors.toList()));
    return property;
  }
}
