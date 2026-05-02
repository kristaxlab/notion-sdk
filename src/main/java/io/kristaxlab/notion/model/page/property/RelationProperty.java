package io.kristaxlab.notion.model.page.property;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelationProperty extends PageProperty {
  private final String type = PagePropertyType.RELATION.type();

  private Boolean hasMore;

  private List<RelationValue> relation;

  @Getter
  @Setter
  public static class RelationValue {
    private String id;
  }
}
