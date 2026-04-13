package io.kristaxlab.notion.model.page.property;

import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TitleProperty extends PageProperty {
  public static final String NAME = PagePropertyType.TITLE.type();
  private final String type = PagePropertyType.TITLE.type();

  private List<RichText> title;
}
