package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.users.User;
import io.kristixlab.notion.api.util.PagePropertyType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PeopleProperty extends PageProperty {
  private final String type = PagePropertyType.PEOPLE.type();

  @JsonProperty("people")
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
