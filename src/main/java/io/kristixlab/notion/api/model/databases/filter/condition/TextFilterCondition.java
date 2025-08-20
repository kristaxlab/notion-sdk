package io.kristixlab.notion.api.model.databases.filter.condition;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TextFilterCondition {
  @JsonProperty("contains")
  private String contains;

  @JsonProperty("does_not_contain")
  private String doesNotContain;

  @JsonProperty("equals")
  private String equals;

  @JsonProperty("does_not_equal")
  private String doesNotEqual;

  @JsonProperty("starts_with")
  private String startsWith;

  @JsonProperty("ends_with")
  private String endsWith;

  @JsonProperty("is_empty")
  private Boolean isEmpty;

  @JsonProperty("is_not_empty")
  private Boolean isNotEmpty;
}
