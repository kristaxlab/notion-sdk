package io.kristixlab.notion.api.model.databases.filter.condition;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NumberFilterCondition {
  @JsonProperty("equals")
  private Number equals;

  @JsonProperty("does_not_equal")
  private Number doesNotEqual;

  @JsonProperty("is_empty")
  private Boolean isEmpty;

  @JsonProperty("is_not_empty")
  private Boolean isNotEmpty;

  @JsonProperty("greater_than")
  private Number greaterThan;

  @JsonProperty("greater_than_or_equal_to")
  private Number greaterThanOrEqualTo;

  @JsonProperty("less_than")
  private Number lessThan;

  @JsonProperty("less_than_or_equal_to")
  private Number lessThanOrEqualTo;
}