package io.kristaxlab.notion.model.page;

import io.kristaxlab.notion.model.common.Parent;
import lombok.Getter;
import lombok.Setter;

/** JSON body for {@code POST /pages/{id}/move}: new {@link Parent} only. */
@Getter
@Setter
public class MovePageParams {

  private Parent parent;
}
