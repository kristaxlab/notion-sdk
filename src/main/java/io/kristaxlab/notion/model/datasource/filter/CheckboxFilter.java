package io.kristaxlab.notion.model.datasource.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Database filter for checkbox properties. Jackson will identify this type by the presence of the
 * "checkbox" field.
 */
@Getter
@Setter
public class CheckboxFilter extends Filter {

  private CheckboxFilterCondition checkbox;

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

  @Data
  public static class CheckboxFilterCondition {

    @JsonProperty("equals")
    private Boolean equals;

    @JsonProperty("does_not_equal")
    private Boolean doesNotEqual;
  }
}
