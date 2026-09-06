package io.kristaxlab.notion.model.database;

import io.kristaxlab.notion.model.common.Cover;
import io.kristaxlab.notion.model.common.Icon;
import io.kristaxlab.notion.model.common.NotionObject;
import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a Notion database object. Contains metadata and the list of child data sources.
 *
 * <p>{@code databaseType} is the database type of a typed database, or {@code null} for a regular
 * database.
 */
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

  /** Database type of a typed database; {@code null} for a regular database. */
  private String databaseType;
}
