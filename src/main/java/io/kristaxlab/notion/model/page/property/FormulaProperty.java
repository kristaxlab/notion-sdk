package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristaxlab.notion.model.common.DateData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormulaProperty extends PagePropertyValue {

  private final String type = PropertyType.FORMULA.type();

  private FormulaValue formula;

  @Getter
  @Setter
  public static class FormulaValue {

    private String type; // date, number, string, boolean

    private String string;

    private Double number;

    @JsonProperty("boolean")
    private Boolean booleanValue;

    private DateData date;
  }
}
