package io.kristaxlab.notion.model.datasource.filter.condition;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFilterCondition {

  private UUID contains;

  private UUID doesNotContain;

  private Boolean isEmpty;

  private Boolean isNotEmpty;
}
