package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import lombok.Getter;
import lombok.Setter;

/** Database property for rich text columns. Allows formatted text content. */
@Getter
@Setter
public class RichTextSchema extends DataSourcePropertySchema {

  public RichTextSchema() {
    setType(PropertyType.RICH_TEXT.type());
    richText = new Object();
  }

  private Object richText;
}
