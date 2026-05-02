package io.kristaxlab.notion.model.page.property;

import io.kristaxlab.notion.model.common.DateData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateProperty extends PageProperty {
  private final String type = PagePropertyType.DATE.type();

  private DateData date;
}
