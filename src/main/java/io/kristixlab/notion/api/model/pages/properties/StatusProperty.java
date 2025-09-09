package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StatusProperty extends PageProperty {
  private final String type = "status";

  @JsonProperty("status")
  private StatusValue status;

  @Data
  public static class StatusValue {
    private String id;
    private String name;
    private String color;
  }
}
