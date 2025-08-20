package io.kristixlab.notion.api.model.databases.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.databases.filter.condition.UserFilterCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LastEditedByDatabaseFilter extends DatabaseFilter {

  @JsonProperty("last_edited_by")
  private UserFilterCondition lastEditedBy;
}
