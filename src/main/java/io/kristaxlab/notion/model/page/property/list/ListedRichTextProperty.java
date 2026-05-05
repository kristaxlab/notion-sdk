package io.kristaxlab.notion.model.page.property.list;

import io.kristaxlab.notion.model.common.richtext.RichText;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListedRichTextProperty extends ListedPageProperty {

  private RichText richText;
}
