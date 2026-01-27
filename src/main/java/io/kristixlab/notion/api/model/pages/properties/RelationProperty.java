package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.util.PagePropertyType;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RelationProperty extends PageProperty {
  private final String type = PagePropertyType.RELATION.type();

  @JsonProperty("has_more")
  private Boolean hasMore;

  @JsonProperty("relation")
  private List<RelationValue> relation;

  @Data
  public static class RelationValue {
    private String id;
  }
}
