package io.kristixlab.notion.api.model.datasources.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.datasources.filter.condition.UserFilterCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class PeopleFilter extends Filter {

  @JsonProperty("people")
  private UserFilterCondition people;

  public static PeopleFilter isEmpty() {
    PeopleFilter filter = new PeopleFilter();
    UserFilterCondition condition = new UserFilterCondition();
    condition.setIsEmpty(true);
    filter.setPeople(condition);
    return filter;
  }

  public static PeopleFilter isNotEmpty() {
    PeopleFilter filter = new PeopleFilter();
    UserFilterCondition condition = new UserFilterCondition();
    condition.setIsNotEmpty(true);
    filter.setPeople(condition);
    return filter;
  }

  public static PeopleFilter contains(UUID value) {
    PeopleFilter filter = new PeopleFilter();
    UserFilterCondition condition = new UserFilterCondition();
    condition.setContains(value);
    filter.setPeople(condition);
    return filter;
  }

  public static PeopleFilter doesNotContain(UUID value) {
    PeopleFilter filter = new PeopleFilter();
    UserFilterCondition condition = new UserFilterCondition();
    condition.setDoesNotContain(value);
    filter.setPeople(condition);
    return filter;
  }
}
