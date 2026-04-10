package io.kristixlab.notion.api.model.page.property;

import static io.kristixlab.notion.api.model.common.richtext.RichText.*;

import io.kristixlab.notion.api.model.common.richtext.RichText;
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
