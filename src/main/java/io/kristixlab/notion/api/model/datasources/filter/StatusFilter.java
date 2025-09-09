package io.kristixlab.notion.api.model.datasources.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.datasources.filter.condition.EqualsEmptyFilterCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StatusFilter extends Filter {

  @JsonProperty("status")
  private EqualsEmptyFilterCondition select;
}
