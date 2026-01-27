package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.DateData;
import io.kristixlab.notion.api.util.PagePropertyType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FormulaProperty extends PageProperty {

  private final String type = PagePropertyType.FORMULA.type();

  @JsonProperty("formula")
  private FormulaValue formula;

  @Data
  public static class FormulaValue {

    @JsonProperty("type")
    private String type; // date, number, string, boolean

    @JsonProperty("string")
    private String string;

    @JsonProperty("number")
    private Double number;

    @JsonProperty("boolean")
    private Boolean booleanValue;

    @JsonProperty("date")
    private DateData date;
  }
}
