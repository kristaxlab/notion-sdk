package io.kristixlab.notion.api.model.page.property.list;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListedTitleProperty extends ListedPageProperty {

  private RichText title;
}
