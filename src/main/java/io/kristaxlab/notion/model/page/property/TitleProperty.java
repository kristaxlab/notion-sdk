package io.kristaxlab.notion.model.page.property;

import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TitleProperty extends PagePropertyValue {
  public static final String NAME = PropertyType.TITLE.type();
  private final String type = PropertyType.TITLE.type();

  private List<RichText> title;
}
