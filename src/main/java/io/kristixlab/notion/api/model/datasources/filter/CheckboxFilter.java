package io.kristixlab.notion.api.model.datasources.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database filter for checkbox properties. Jackson will identify this type by the presence of the
 * "checkbox" field.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CheckboxFilter extends Filter {

  @JsonProperty("checkbox")
  private CheckboxFilterCondition checkbox;

  @Data
  public static class CheckboxFilterCondition {

    @JsonProperty("equals")
    private Boolean equals;

    @JsonProperty("does_not_equal")
    private Boolean doesNotEqual;
  }

  public static CheckboxFilter isChecked() {
    CheckboxFilter filter = new CheckboxFilter();
    CheckboxFilterCondition condition = new CheckboxFilterCondition();
    condition.setEquals(true);
    filter.setCheckbox(condition);
    return filter;
  }

  public static CheckboxFilter isUnchecked() {
    CheckboxFilter filter = new CheckboxFilter();
    CheckboxFilterCondition condition = new CheckboxFilterCondition();
    condition.setEquals(false);
    filter.setCheckbox(condition);
    return filter;
  }
}
