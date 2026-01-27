package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.users.User;
import io.kristixlab.notion.api.util.PagePropertyType;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PeopleProperty extends PageProperty {
  private final String type = PagePropertyType.PEOPLE.type();

  @JsonProperty("people")
  private List<User> people;
}
