package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UniqueIdProperty extends PageProperty {
  private final String type = "unique_id";

  @JsonProperty("unique_id")
  private UniqueIdValue uniqueId;

  @Data
  public static class UniqueIdValue {
    private Integer number;
    private String prefix;
  }
}
