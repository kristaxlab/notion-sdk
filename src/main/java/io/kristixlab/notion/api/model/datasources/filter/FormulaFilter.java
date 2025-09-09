package io.kristixlab.notion.api.model.datasources.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.datasources.filter.condition.DateFilterCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FormulaFilter extends Filter {

  @JsonProperty("formula")
  private FormulaFilterCondition formula;

  @Data
  public class FormulaFilterCondition {
    @JsonProperty("checkbox")
    private CheckboxFilter checkbox;

    @JsonProperty("date")
    private DateFilterCondition date;

    @JsonProperty("number")
    private NumberFilter number;

    @JsonProperty("string")
    private RichTextFilter string;
  }
}
