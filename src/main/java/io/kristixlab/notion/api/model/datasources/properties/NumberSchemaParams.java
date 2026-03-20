package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Database property for number columns. Supports different number formats. */
@Data
@EqualsAndHashCode(callSuper = true)
public class NumberSchemaParams extends DataSourcePropertySchemaParams {

  @JsonProperty("number")
  private NumberFormat number = new NumberFormat();

  public static NumberSchemaParams of(NumberFormatType format) {
    return of(format.getValue());
  }

  public static NumberSchemaParams of(String format) {
    NumberSchemaParams property = new NumberSchemaParams();
    NumberFormat numberFormat = new NumberFormat();
    numberFormat.setFormat(format);
    property.setNumber(numberFormat);
    return property;
  }

  @Data
  public static class NumberFormat {
    @JsonProperty("format")
    private String format;
  }
}
