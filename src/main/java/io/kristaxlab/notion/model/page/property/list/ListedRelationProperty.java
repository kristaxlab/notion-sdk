package io.kristaxlab.notion.model.page.property.list;

import io.kristaxlab.notion.model.common.BlockReference;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListedRelationProperty extends ListedPageProperty {

  private BlockReference relation;
}
