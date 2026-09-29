package io.kristaxlab.notion.model.page.property;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelationProperty extends PagePropertyValue {
  private final String type = PropertyType.RELATION.type();

  private Boolean hasMore;

  private List<RelationValue> relation;

  @Getter
  @Setter
  public static class RelationValue {
    private String id;
  }
}
