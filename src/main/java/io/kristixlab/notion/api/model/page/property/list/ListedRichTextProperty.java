package io.kristixlab.notion.api.model.page.property.list;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListedRichTextProperty extends ListedPageProperty {

  private RichText richText;
}
