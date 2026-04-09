package io.kristixlab.notion.api.model.page.property.list;

import io.kristixlab.notion.api.model.common.BlockReference;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListedRelationProperty extends ListedPageProperty {

  private BlockReference relation;
}
