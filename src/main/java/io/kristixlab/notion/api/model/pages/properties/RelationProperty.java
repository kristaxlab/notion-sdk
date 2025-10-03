package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class RelationProperty extends PageProperty {
  private final String type = "relation";

  @JsonProperty("has_more")
  private Boolean hasMore;

  @JsonProperty("relation")
  private List<RelationValue> relation;

  @Data
  public static class RelationValue {
    private String id;
  }
}
