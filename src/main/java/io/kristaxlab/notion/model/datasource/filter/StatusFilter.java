package io.kristaxlab.notion.model.datasource.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.datasources.filter.condition.EqualsEmptyFilterCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusFilter extends Filter {

  @JsonProperty("status")
  private EqualsEmptyFilterCondition status;
}
