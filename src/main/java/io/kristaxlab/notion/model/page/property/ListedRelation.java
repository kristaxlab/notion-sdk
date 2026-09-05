package io.kristaxlab.notion.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class ListedRelation extends ListedItem {

  private RelationProperty.RelationValue relation;
}
