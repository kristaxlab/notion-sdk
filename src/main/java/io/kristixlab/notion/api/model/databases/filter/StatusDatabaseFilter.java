package io.kristixlab.notion.api.model.databases.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.databases.filter.condition.EqualsEmptyFilterCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StatusDatabaseFilter extends DatabaseFilter {

  @JsonProperty("status")
  private EqualsEmptyFilterCondition select;
}
