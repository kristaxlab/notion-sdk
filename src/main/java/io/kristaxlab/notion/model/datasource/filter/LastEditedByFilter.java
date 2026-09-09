package io.kristaxlab.notion.model.datasource.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristaxlab.notion.model.datasource.filter.condition.UserFilterCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LastEditedByFilter extends Filter {

  @JsonProperty("last_edited_by")
  private UserFilterCondition lastEditedBy;

  public static LastEditedByFilter contains(java.util.UUID value) {
    LastEditedByFilter filter = new LastEditedByFilter();
    UserFilterCondition condition = new UserFilterCondition();
    condition.setContains(value);
    filter.setLastEditedBy(condition);
    return filter;
  }

  public static LastEditedByFilter doesNotContain(java.util.UUID value) {
    LastEditedByFilter filter = new LastEditedByFilter();
    UserFilterCondition condition = new UserFilterCondition();
    condition.setDoesNotContain(value);
    filter.setLastEditedBy(condition);
    return filter;
  }
}
