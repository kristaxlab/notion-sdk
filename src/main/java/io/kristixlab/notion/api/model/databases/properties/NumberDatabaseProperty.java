package io.kristixlab.notion.api.model.databases.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Database property for number columns. Supports different number formats. */
@Data
@EqualsAndHashCode(callSuper = true)
public class NumberDatabaseProperty extends DatabaseProperty {

  @JsonProperty("number")
  private NumberFormat number;

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
}
