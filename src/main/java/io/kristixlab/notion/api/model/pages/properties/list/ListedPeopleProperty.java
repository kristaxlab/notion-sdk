package io.kristixlab.notion.api.model.pages.properties.list;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.users.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListedPeopleProperty extends ListedPageProperty {

  @JsonProperty("people")
  private User people;

}
