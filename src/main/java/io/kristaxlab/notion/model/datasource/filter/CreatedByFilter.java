package io.kristaxlab.notion.model.datasource.filter;

import io.kristaxlab.notion.model.datasource.filter.condition.UserFilterCondition;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatedByFilter extends Filter {

  private UserFilterCondition createdBy;

  public static CreatedByFilter contains(UUID value) {
    CreatedByFilter filter = new CreatedByFilter();
    UserFilterCondition condition = new UserFilterCondition();
    condition.setContains(value);
    filter.setCreatedBy(condition);
    return filter;
  }

  public static CreatedByFilter doesNotContain(UUID value) {
    CreatedByFilter filter = new CreatedByFilter();
    UserFilterCondition condition = new UserFilterCondition();
    condition.setDoesNotContain(value);
    filter.setCreatedBy(condition);
    return filter;
  }
}
