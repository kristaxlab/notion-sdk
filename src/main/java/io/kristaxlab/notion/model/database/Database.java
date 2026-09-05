package io.kristaxlab.notion.model.database;

import io.kristaxlab.notion.model.common.Cover;
import io.kristaxlab.notion.model.common.Icon;
import io.kristaxlab.notion.model.common.NotionObject;
import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** Represents a Notion database object. Contains the database schema, properties, and metadata. */
@Getter
@Setter
public class Database extends NotionObject {

  private List<RichText> title;

  private List<RichText> description;

  private Icon icon;

  private Cover cover;

  private String url;

  private String publicUrl;

  private Boolean isInline;

  private Boolean isLocked;

  private List<DataSourceRef> dataSources;
}
