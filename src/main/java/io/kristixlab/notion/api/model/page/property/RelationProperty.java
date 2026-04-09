package io.kristixlab.notion.api.model.page.property;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelationProperty extends PageProperty {
  private final String type = PagePropertyType.RELATION.type();

  private Boolean hasMore;

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

  @Getter
  @Setter
  public static class RelationValue {
    private String id;
  }
}
