package io.kristaxlab.notion.model.datasource;

import io.kristaxlab.notion.model.common.Cover;
import io.kristaxlab.notion.model.common.Icon;
import io.kristaxlab.notion.model.common.NotionObject;
import io.kristaxlab.notion.model.common.Parent;
import io.kristaxlab.notion.model.common.richtext.RichText;
import io.kristaxlab.notion.model.datasource.properties.DataSourcePropertySchema;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a Notion data source object. Contains the data source schema, properties, and
 * metadata.
 */
@Getter
@Setter
public class DataSource extends NotionObject {

  private List<RichText> title;

  private List<RichText> description;

  private Parent databaseParent;

  private Icon icon;

  private Cover cover;

  private Map<String, DataSourcePropertySchema> properties;

  private Boolean isInline;

  private String url;

  private String publicUrl;
}
