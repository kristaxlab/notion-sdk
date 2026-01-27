package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.util.PagePropertyType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UniqueIdProperty extends PageProperty {
  private final String type = PagePropertyType.UNIQUE_ID.type();

  @JsonProperty("unique_id")
  private UniqueIdValue uniqueId;

  @Data
  public static class UniqueIdValue {
    private Integer number;
    private String prefix;
  }
}
