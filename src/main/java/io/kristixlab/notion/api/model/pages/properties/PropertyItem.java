package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PropertyItem extends PageProperty {

  private final String type = "property_item";

  @JsonProperty("property_item")
  private PropertyItemValue propertyItem;

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class PropertyItemValue {
    @JsonProperty("id")
    private String id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("next_url")
    private String nextUrl;
  }
}
