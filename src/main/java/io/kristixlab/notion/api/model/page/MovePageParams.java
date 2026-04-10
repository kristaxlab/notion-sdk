package io.kristixlab.notion.api.model.page;

import io.kristixlab.notion.api.model.common.Parent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovePageParams {

  private Parent parent;
}
