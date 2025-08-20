package io.kristixlab.notion.api.model.databases.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database filter for checkbox properties. Jackson will identify this type by the presence of the
 * "checkbox" field.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CheckboxDatabaseFilter extends DatabaseFilter {

  @JsonProperty("checkbox")
  private CheckboxFilterCondition checkbox;

  @Data
  public static class CheckboxFilterCondition {

    @JsonProperty("equals")
    private Boolean equals;

    @JsonProperty("does_not_equal")
    private Boolean doesNotEqual;
  }
}
