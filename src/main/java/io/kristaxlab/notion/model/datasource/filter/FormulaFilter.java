package io.kristaxlab.notion.model.datasource.filter;

import io.kristaxlab.notion.model.datasource.filter.condition.DateFilterCondition;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormulaFilter extends Filter {

  private FormulaFilterCondition formula;

  @Data
  public class FormulaFilterCondition {

    private CheckboxFilter checkbox;

    private DateFilterCondition date;

    private NumberFilter number;

    private RichTextFilter string;
  }
}
