package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.util.PagePropertyType;
import java.util.Arrays;
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

  /** Creates a property from one or more related page IDs. */
  public static RelationProperty of(String... pageIds) {
    RelationProperty property = new RelationProperty();
    property.setRelation(
        Arrays.stream(pageIds)
            .map(
                id -> {
                  RelationValue v = new RelationValue();
                  v.setId(id);
                  return v;
                })
            .collect(java.util.stream.Collectors.toList()));
    return property;
  }

  @Data
  public static class RelationValue {
    private String id;
  }
}
