package io.kristaxlab.notion.model.page.property;

import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RichTextProperty extends PagePropertyValue {
  private final String type = PropertyType.RICH_TEXT.type();

  private List<RichText> richText = new ArrayList<>();
}
