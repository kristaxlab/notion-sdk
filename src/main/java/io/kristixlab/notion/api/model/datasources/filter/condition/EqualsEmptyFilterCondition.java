package io.kristixlab.notion.api.model.datasources.filter.condition;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EqualsEmptyFilterCondition {

  @JsonProperty("equals")
  private String equals;

  @JsonProperty("does_not_equal")
  private String doesNotEqual;

  @JsonProperty("is_empty")
  private Boolean isEmpty;

  @JsonProperty("is_not_empty")
  private Boolean isNotEmpty;
}
