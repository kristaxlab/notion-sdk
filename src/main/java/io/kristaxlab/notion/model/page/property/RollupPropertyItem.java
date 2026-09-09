package io.kristaxlab.notion.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class RollupPropertyItem extends PropertyItem {

  private RollupProperty.RollupValue rollup;
}
