package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Database property for number columns. Supports different number formats. */
@Data
@EqualsAndHashCode(callSuper = true)
public class NumberDatasourceProperty extends DatasourceProperty {

  @JsonProperty("number")
  private NumberFormat number = new NumberFormat();

  @Data
  public static class NumberFormat {
    @JsonProperty("format")
    private String
        format; // "number", "number_with_commas", "percent", "dollar", "canadian_dollar", "euro",
    // "pound", "yen", "ruble", "rupee", "won", "yuan", "real", "lira", "rupiah",
    // "franc", "hong_kong_dollar", "new_zealand_dollar", "krona", "norwegian_krone",
    // "mexican_peso", "rand", "new_taiwan_dollar", "danish_krone", "zloty", "baht",
    // "forint", "koruna", "shekel", "chilean_peso", "philippine_peso", "dirham",
    // "colombian_peso", "riyal", "ringgit", "leu", "argentine_peso", "uruguayan_peso",
    // "singapore_dollar"
  }

  public static NumberDatasourceProperty of(io.kristixlab.notion.api.model.datasources.NumberFormat format) {
    return of(format.toString());
  }

  public static NumberDatasourceProperty of(String format) {
    NumberDatasourceProperty property = new NumberDatasourceProperty();
    NumberFormat numberFormat = new NumberFormat();
    numberFormat.setFormat(format);
    property.setNumber(numberFormat);
    return property;
  }
}
