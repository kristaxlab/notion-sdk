package io.kristaxlab.notion.model.datasource.filter;

import io.kristixlab.notion.api.model.datasources.filter.condition.UserFilterCondition;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeopleFilter extends Filter {

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
