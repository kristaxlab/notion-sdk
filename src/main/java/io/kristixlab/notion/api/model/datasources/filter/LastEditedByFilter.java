package io.kristixlab.notion.api.model.datasources.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.datasources.filter.condition.UserFilterCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
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
