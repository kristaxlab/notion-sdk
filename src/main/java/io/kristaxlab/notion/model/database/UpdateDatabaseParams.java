package io.kristaxlab.notion.model.database;

import io.kristaxlab.notion.model.common.Cover;
import io.kristaxlab.notion.model.common.Icon;
import io.kristaxlab.notion.model.common.Parent;
import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDatabaseParams {

  private Parent parent;

  private List<RichText> title;

  private List<RichText> description;

  private Boolean isInline;

  private Icon icon;

  private Cover cover;

  private Boolean inTrash;

  private Boolean isLocked;
}
