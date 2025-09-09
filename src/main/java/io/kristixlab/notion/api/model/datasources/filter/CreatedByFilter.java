package io.kristixlab.notion.api.model.datasources.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.datasources.filter.condition.UserFilterCondition;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreatedByFilter extends Filter {

  @JsonProperty("created_by")
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
