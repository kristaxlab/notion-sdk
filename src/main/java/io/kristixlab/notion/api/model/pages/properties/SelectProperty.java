package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SelectProperty extends PageProperty {
  private final String type = "select";
  @JsonProperty("select")
  private SelectValue select;

  @Data
  public static class SelectValue {
    private String id;
    private String name;
    private String color;
  }
}
