package io.kristixlab.notion.api.model.databases.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class IdDatabaseFilter extends DatabaseFilter {

  @JsonProperty("unique_id")
  private IdFilterCondition uniqueId;

  @Data
  public static class IdFilterCondition {
    @JsonProperty("equals")
    private Number equals;

    @JsonProperty("does_not_equal")
    private Number doesNotEqual;

    @JsonProperty("greater_than")
    private Number greaterThan;

    @JsonProperty("greater_than_or_equal_to")
    private Number greaterThanOrEqualTo;

    @JsonProperty("less_than")
    private Number lessThan;

    @JsonProperty("less_than_or_equal_to")
    private Number lessThanOrEqualTo;
  }
}
