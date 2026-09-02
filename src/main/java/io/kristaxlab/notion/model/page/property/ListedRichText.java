package io.kristaxlab.notion.model.page.property;

import io.kristaxlab.notion.model.common.richtext.RichText;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class ListedRichText extends ListedItem {

  private RichText richText;
}
