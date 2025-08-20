package io.kristixlab.notion.api.model.databases.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.databases.filter.condition.DateFilterCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FormulaDatabaseFilter extends DatabaseFilter {

  @JsonProperty("formula")
  private FormulaFilterCondition formula;

  @Data
  public class FormulaFilterCondition {
    @JsonProperty("checkbox")
    private CheckboxDatabaseFilter checkbox;
    @JsonProperty("date")
    private DateFilterCondition date;
    @JsonProperty("number")
    private NumberDatabaseFilter number;
    @JsonProperty("string")
    private RichTextDatabaseFilter string;
  }
}
